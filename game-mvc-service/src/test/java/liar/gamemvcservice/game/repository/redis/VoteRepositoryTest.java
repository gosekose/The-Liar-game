package liar.gamemvcservice.game.repository.redis;

import liar.gamemvcservice.exception.exception.RedisLockException;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameTurn;
import liar.gamemvcservice.game.domain.Vote;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class VoteRepositoryTest {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    RedissonClient redissonClient;

    private Game game;
    int duplicatedCnt = 0;

    @BeforeEach
    public void init() {
        SetUpGameDto setUpGameDto = new SetUpGameDto("1", "1", "1", Arrays.asList("1", "2", "3", "4", "5"));
        game = Game.of(setUpGameDto);
        Game save = gameRepository.save(game);
        duplicatedCnt = 0;
    }

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
        voteRepository.deleteAll();
        duplicatedCnt = 0;
    }

    @Test
    @DisplayName("save")
    public void save() throws Exception {
        //given
        Vote vote = new Vote(game.getId(), game.getPlayerIds());
        ;
        //when
        Vote savedVote = voteRepository.save(vote);
        Vote findVote = voteRepository.findVoteByGameId(game.getId());

        //then
        assertThat(savedVote.getGameId()).isEqualTo(game.getId());
    }

    @Test
    @DisplayName("멀티 스레드 상황에서 service 단위 트렌젝션이 없다면 원자성을 보장하지 않는다.")
    public void save_thread_not_safe() throws Exception {
        Vote vote = new Vote(game.getId(), game.getPlayerIds());

        //when
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {

                if (voteRepository.findVoteByGameId(vote.getGameId()) == null) {
                    System.out.println(vote);
                    voteRepository.save(vote);
                    duplicatedCnt++;
                }

            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        //then
        Vote savedVote = voteRepository.findVoteByGameId(vote.getGameId());
        assertThat(savedVote.toString()).isEqualTo(vote.toString());
        assertThat(duplicatedCnt).isNotEqualTo(1);

    }

    @Test
    @DisplayName("멀티 스레드 상황에서 vote 저장하기")
    public void save_thread() throws Exception {
        //given
        Vote vote = new Vote(game.getId(), game.getPlayerIds());

        //when
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {saveOnce(vote);});
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        //then
        Vote savedVote = voteRepository.findVoteByGameId(vote.getGameId());
        assertThat(savedVote.toString()).isEqualTo(vote.toString());
        assertThat(duplicatedCnt).isEqualTo(1);
    }

    @Transactional
    void saveOnce(Vote vote) {

        String lockKey = getLockKey(vote.getId());
        RLock lock = redissonClient.getLock(lockKey);

        try{
            boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new RedisLockException();
            }

            if (voteRepository.findVoteByGameId(vote.getGameId()) == null) {
                System.out.println(vote);
                voteRepository.save(vote);
                duplicatedCnt++;
            }


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    private <T> String getLockKey(T arg) {

        if (arg instanceof String) {
            return (String) arg;
        }

        else if (arg instanceof Game){
            return ((Game) arg).getId();
        }

        else if (arg instanceof GameTurn) {
            return ((GameTurn) arg).getId();
        }

        else if (arg instanceof Vote) {
            return ((Vote) arg).getId();
        }

        else if (arg instanceof Object[]) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : (Object[]) arg) {
                sb.append(getLockKey(obj));
            }
            return sb.toString();
        }
        throw new RedisLockException();
    }



}