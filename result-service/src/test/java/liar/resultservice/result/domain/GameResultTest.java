package liar.resultservice.result.domain;

import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GameResultTest {

    @Autowired
    TopicRepository topicRepository;

    private Topic topic;

    @BeforeEach
    public void init() {
        topic = topicRepository.save(new Topic("game"));
    }

    @Test
    @DisplayName("gameResult 생성.")
    public void saveGameResult() throws Exception {
        //given
        GameResult gameResult = GameResult.builder()
                .topic(topic)
                .gameId("!")
                .gameName("game")
                .roomId("room1")
                .winner(GameRole.LIAR)
                .totalUsers(7)
                .build();


        //then
        assertThat(gameResult.getId()).isInstanceOf(String.class);

    }
}