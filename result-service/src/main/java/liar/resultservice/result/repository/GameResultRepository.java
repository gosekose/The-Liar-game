package liar.resultservice.result.repository;

import liar.resultservice.result.domain.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, String> {
    GameResult findGameResultByGameId(String gameId);
}
