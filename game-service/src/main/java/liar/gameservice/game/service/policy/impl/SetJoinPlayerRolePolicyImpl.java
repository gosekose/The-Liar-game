package liar.gameservice.game.service.policy.impl;

import liar.gameservice.game.domain.Game;
import liar.gameservice.game.domain.GameRole;
import liar.gameservice.game.domain.JoinPlayer;
import liar.gameservice.game.domain.Player;
import liar.gameservice.game.service.policy.interfaces.SetJoinPlayerRolePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class SetJoinPlayerRolePolicyImpl implements SetJoinPlayerRolePolicy {

    private final ReactiveRedisTemplate<String, JoinPlayer> reactiveRedisTemplate;

    @Override
    public void validateAuthenticationToSetRole() {

    }

    @Override
    public Flux<JoinPlayer> setRoleToPlayer(Game game) {
        int random = (int) (Math.random() * game.getPlayerIds().size());
        return Flux.fromIterable(game.getPlayerIds())
                .map(playerId -> {
                    GameRole role = game.getPlayerIds().get(random).equals(playerId) ? GameRole.LIAR : GameRole.CITIZEN;
                    JoinPlayer joinPlayer = new JoinPlayer(game.getRoomId(), new Player(playerId, role));
                    reactiveRedisTemplate.opsForValue().set("JoinPlayer:" + joinPlayer.getId(), joinPlayer);
                    return joinPlayer;
                });
    }
}
