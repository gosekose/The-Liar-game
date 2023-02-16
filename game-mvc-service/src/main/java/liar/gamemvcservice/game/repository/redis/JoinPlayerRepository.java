package liar.gamemvcservice.game.repository.redis;

import liar.gamemvcservice.game.domain.JoinPlayer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JoinPlayerRepository extends CrudRepository<JoinPlayer, String> {

    List<JoinPlayer> findByGameId(String gameId);
}
