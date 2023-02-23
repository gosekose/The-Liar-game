package liar.resultservice.result.repository;

import liar.resultservice.result.domain.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, AtomicLong> {

    GameResult findGameResultByGameId(String gameId);

}
