package liar.resultservice.result.repository;

import liar.resultservice.exception.exception.RedisLockException;
import liar.resultservice.result.domain.GameResult;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, String> {

    GameResult findGameResultByGameId(String gameId);
}
