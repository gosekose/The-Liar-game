package liar.gamemvcservice.game.repository;

import liar.gamemvcservice.game.domain.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, String> {
}
