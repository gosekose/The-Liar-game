package liar.resultservice.result.repository;

import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.PlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerResultRepository extends JpaRepository<PlayerResult, String> {

    List<PlayerResult> findPlayerResultsByGameResult(GameResult gameResult);

}
