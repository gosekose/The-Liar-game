package liar.gamemvcservice.game.service.vote;

import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.Vote;
import liar.gamemvcservice.game.domain.VotedResult;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import liar.gamemvcservice.game.service.ThreadServiceOnlyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class VotePolicyTest extends ThreadServiceOnlyTest {

    @Autowired
    VotePolicy votePolicy;
    @Autowired
    VoteRepository voteRepository;

    private Game game;

    @BeforeEach
    public void init() {
        SetUpGameDto setUpGameDto = new SetUpGameDto("1", "1", "1", Arrays.asList("1", "2", "3", "4", "5"));
        game = Game.of(setUpGameDto);
        num = 100;
        threads = new Thread[num];
    }

    @AfterEach
    public void tearDown() {
        voteRepository.deleteAll();
    }

    @Test
    @DisplayName("멀티 스레딩 환경에서 vote를 저장한다.")
    public void saveVote_multiThread() throws Exception {
        //given
        String[] results = new String[num];

        //when
        for (int i = 0; i < num; i++) {
            int finalIndex = i; //lambda 매개변수 유사 final 역할

            threads[i] = new Thread(() -> {
                try {
                    String voteId = votePolicy.saveVote(game);
                    results[finalIndex] = voteId;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        runThreads();
        String result = results[0];

        //then
        for (int i = 0; i < num; i++) {
            assertThat(results[i]).isEqualTo(result);
        }

    }

    @Test
    @DisplayName("단일 스레드 환경에서 liar를 투표한다")
    public void voteLiarUser() throws Exception {
        //given
        num = 5;
        votePolicy.saveVote(game);

        //when
        for (int i = 0; i < num; i++) {
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i + 1), "2");
        }
        List<VotedResult> maxVotedLiarUser = votePolicy.getMostVotedLiarUser(game.getId());

        //then
        assertThat(maxVotedLiarUser.size()).isEqualTo(1);
        assertThat(maxVotedLiarUser.get(0).getLiarId()).isEqualTo("2");
        assertThat(maxVotedLiarUser.get(0).getCnt()).isEqualTo(5);
    }

    @Test
    @DisplayName("멀티 스레딩 환경에서 liar를 투표한다. : RedissonClient")
    public void voteLiarUser_multiThread() throws Exception {
        //given
        num = 5;
        votePolicy.saveVote(game);

        //when
        for (int i = 0; i < num; i++) {
            int finalIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    votePolicy.voteLiarUser(game.getId(), String.valueOf(finalIndex + 1), "2");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        runThreads();
        List<VotedResult> maxVotedLiarUser = votePolicy.getMostVotedLiarUser(game.getId());

        //then
        assertThat(maxVotedLiarUser.size()).isEqualTo(1);
        assertThat(maxVotedLiarUser.get(0).getLiarId()).isEqualTo("2");
        assertThat(maxVotedLiarUser.get(0).getCnt()).isEqualTo(5);
    }

    @Test
    @DisplayName("멀티 스레딩 환경에서 liar 투표 결과를 반환한다.")
    public void getMaxVotedLiarUser_multiThread() throws Exception {
        //given
        num = 5;
        votePolicy.saveVote(game);

        //when
        for (int i = 0; i < num; i++) {
            int finalIndex = i;
            threads[i] = new Thread(() -> {
                try {

                    if (finalIndex  < 2) {
                        votePolicy.voteLiarUser(game.getId(), String.valueOf(finalIndex + 1), "2");
                    }
                    else if (finalIndex < 4) {
                        votePolicy.voteLiarUser(game.getId(), String.valueOf(finalIndex + 1), "1");
                    }
                    else {
                        votePolicy.voteLiarUser(game.getId(), String.valueOf(finalIndex + 1), "3");
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        runThreads();
        List<VotedResult> maxVotedLiarUser = votePolicy.getMostVotedLiarUser(game.getId());

        //then
        assertThat(maxVotedLiarUser.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("멀티 스레딩 환경에서 liar를 투표한다. : synchronized")
    public void voteUser_synchronized() throws Exception {
        //given
        num = 5;
        votePolicy.saveVote(game);

        //when
        for (int i = 0; i < num; i++) {
            int finalIndex = i;
            threads[i] = new Thread(() -> {
                voteLiarUser(game.getId(), String.valueOf(finalIndex + 1), "2");
            });
        }

        runThreads();
        List<VotedResult> maxVotedLiarUser = votePolicy.getMostVotedLiarUser(game.getId());

        //then
        assertThat(maxVotedLiarUser.size()).isEqualTo(1);
        assertThat(maxVotedLiarUser.get(0).getLiarId()).isEqualTo("2");
        assertThat(maxVotedLiarUser.get(0).getCnt()).isEqualTo(5);
    }

    /**
     * Syncronized와 성능 비교를 위한 테스트
     */
    private synchronized void voteLiarUser(String gameId, String userId, String liarId) {
        Vote vote = voteRepository.findVoteByGameId(gameId);
        vote.updateVoteResults(userId, liarId);
        voteRepository.save(vote);
    }

}