package liar.gamemvcservice.game.repository.redis;

import liar.gamemvcservice.game.domain.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, String> {
}
