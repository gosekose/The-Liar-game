package liar.gameservice.game.service.policy.impl;

import liar.gameservice.game.domain.Game;
import liar.gameservice.game.domain.GameRole;
import liar.gameservice.game.domain.JoinPlayer;
import liar.gameservice.game.domain.Player;
import liar.gameservice.game.service.policy.interfaces.SetJoinPlayerRolePolicy;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class SetJoinPlayerJoinPlayerRolePolicyImpl implements SetJoinPlayerRolePolicy {

    @Override
    public void validateAuthenticationToSetRole() {

    }

    @Override
    public Flux<JoinPlayer> setRoleToPlayer(Game game) {
        int random = (int) (Math.random() * game.getPlayerIds().size());
        return Flux.fromIterable(game.getPlayerIds())
                .map(playerId -> {
                    GameRole role = random == 0 ? GameRole.LIAR : GameRole.CITIZEN;
                    return new JoinPlayer(game.getRoomId(), new Player(playerId, role));
                });
    }
}
