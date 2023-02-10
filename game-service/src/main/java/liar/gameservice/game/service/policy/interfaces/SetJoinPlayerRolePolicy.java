package liar.gameservice.game.service.policy.interfaces;

import liar.gameservice.game.domain.Game;
import liar.gameservice.game.domain.JoinPlayer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public interface SetJoinPlayerRolePolicy {

    void validateAuthenticationToSetRole();

    Flux<JoinPlayer> setRoleToPlayer(Game game);
}
