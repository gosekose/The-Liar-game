package liar.gamemvcservice.game.service.vote;

import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VotePolicyImplTest {

    @Autowired
    VotePolicy votePolicy;

    @Autowired
    VotePolicyImpl votePolicyImpl;

    @Autowired
    VoteRepository voteRepository;

    private Game game;

    @BeforeEach
    public void init() {
        SetUpGameDto setUpGameDto = new SetUpGameDto("1", "1", "1", Arrays.asList("1", "2", "3", "4", "5"));
        game = Game.of(setUpGameDto);
    }

    @AfterEach
    public void tearDown() {
        voteRepository.deleteAll();
    }

    @Test
    @DisplayName("멀티 스레딩 환경에서 vote를 저장한다.")
    public void saveVote() throws Exception {
        //given
        int num = 10;
        Thread[] threads = new Thread[num];

        for (int i = 0; i < num; i++) {
            threads[i] = new Thread(() -> {
                try {
                    String voteId = votePolicy.saveVote(game);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (int i = 0; i < num; i++) threads[i].start();
        for (int i = 0; i < num; i++) threads[i].join();

        //when

        //then

    }

}