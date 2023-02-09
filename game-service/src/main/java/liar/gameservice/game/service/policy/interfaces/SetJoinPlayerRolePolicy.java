package liar.gameservice.game.service.policy.interfaces;

import liar.gameservice.game.domain.Game;
import liar.gameservice.game.domain.JoinPlayer;
import reactor.core.publisher.Flux;

import java.util.List;

public interface SetJoinPlayerRolePolicy {

    void validateAuthenticationToSetRole();

    Flux<JoinPlayer> setRoleToPlayer(Game game);
}
