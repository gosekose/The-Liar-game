package liar.gamemvcservice.game.repository;

import liar.gamemvcservice.game.domain.GameTurn;
import org.springframework.data.repository.CrudRepository;

public interface GameTurnRepository extends CrudRepository<GameTurn, String> {

    GameTurn findGameTurnByGameId(String gameId);

}
