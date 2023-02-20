package liar.gamemvcservice.game.repository.redis;

import liar.gamemvcservice.exception.exception.RedisLockException;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;
    @Autowired
    RedissonClient redissonClient;
    private int duplicatedTotalCnt = 0;

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
    }

    @Test
    @DisplayName("Game을 저장하면, redis에 저장되어야 한다.")
    public void save_game_success() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("1", "1", "1", Arrays.asList("1", "2", "3", "4"));
        Game game = Game.of(setUpGameDto);

        //when
        Game savedGame = gameRepository.save(game);

        //then
        assertThat(savedGame.getId()).isEqualTo(game.getId());
        assertThat(savedGame.getGameName()).isEqualTo(game.getGameName());
        assertThat(savedGame.getHostId()).isEqualTo(game.getHostId());
        assertThat(savedGame.getRoomId()).isEqualTo(game.getRoomId());
        assertThat(savedGame.getPlayerIds()).isEqualTo(game.getPlayerIds());
    }

    @Test
    @DisplayName("스레드 safe 한 상태로 game 단건 저장하기")
    public void saveIfNotExistsGame_ThreadSafe() throws Exception {
        //given
        Thread[] threads = new Thread[100];

        SetUpGameDto setUpGameDto = new SetUpGameDto("1", "1", "1", Arrays.asList("2", "3", "4"));
        Game game = Game.of(setUpGameDto);

        //when
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {

                String lockKey = getLockKey(game.getId());
                RLock lock = redissonClient.getLock(lockKey);

                try {
                    boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
                    if (!isLocked) {
                        System.out.println("락을 획득할 수 없습니다.");
                        throw new RedisLockException();
                    }

                    if (gameRepository.findById(game.getId()).isEmpty()) {
                        System.out.println("game = " + game);
                        gameRepository.save(game);
                        duplicatedTotalCnt++;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("런타임 에러");
                    throw new RuntimeException(e);
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }

            });
        }

        for (int i = 0; i < 100; i++) threads[i].start();
        for (int i = 0; i < 100; i++) threads[i].join();

        //then
        System.out.println("duplicatedTotalCnt = " + duplicatedTotalCnt);
        assertThat(duplicatedTotalCnt).isEqualTo(1);

    }

    private <T> String getLockKey(T arg) {

        if (arg instanceof String) {
            return (String) arg;
        }

        else if (arg instanceof Game) {
            return ((Game) arg).getId();
        }

        throw new IllegalArgumentException();
    }

}