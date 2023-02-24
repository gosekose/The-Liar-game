package liar.resultservice.result.repository;

import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class GameResultRepositoryTest {

    @Autowired
    GameResultRepository gameResultRepository;

    @Autowired
    TopicRepository topicRepository;

    private Topic topic;

    @BeforeEach
    public void init() {
        topic = topicRepository.save(new Topic("game"));
    }

    @Test
    @DisplayName("단일 스레드 상황에서 gameResult save")
    public void save_singleThread() throws Exception {
        //given
        int threadCount = 10;
        String[] gameResultIds = new String[threadCount];

        //when
        for (int i = 0; i < threadCount; i++) {
            gameResultIds[i] = gameResultRepository.save(
                    GameResult.builder()
                            .topic(topic)
                            .gameId("!")
                            .gameName("game")
                            .roomId("room1")
                            .winner(GameRole.LIAR)
                            .totalUsers(7)
                            .build()
            ).getId();
        }

        Set<String> idSet = new HashSet<>();

        for (String id : gameResultIds) {
            System.out.println("id = " + id);
            idSet.add(id);
        }

        //then
        assertThat(idSet.size()).isEqualTo(10);
    }


    @Test
    @DisplayName("멀티 스레드 환경에서 GameResult 생성시 id가 모두 다른 것이 보장 되어야 한다.")
    public void save_multiThread() throws Exception {
        //given
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        String[] gameResultIds = new String[threadCount];

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {

                try {
                    topic = topicRepository.save(new Topic("game"));
                    gameResultIds[finalIdx] = gameResultRepository.save(
                            GameResult.builder()
                                    .topic(topic)
                                    .gameId("!")
                                    .gameName("game")
                                    .roomId("room1")
                                    .winner(GameRole.LIAR)
                                    .totalUsers(7)
                                    .build()).getId();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Set<String> idSet = new HashSet<>();

        for (String id : gameResultIds) {
            System.out.println("gameResult.getId() = " + id);
            idSet.add(id);
        }

        //then
        assertThat(idSet.size()).isEqualTo(threadCount);
    }
}