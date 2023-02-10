package liar.gameservice.game.service.policy.impl;

import liar.gameservice.game.domain.*;
import liar.gameservice.game.service.policy.interfaces.SetJoinPlayerRolePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

@SpringBootTest
class SetJoinPlayerRolePolicyImplTest {

    @Autowired
    SetJoinPlayerRolePolicy setJoinPlayerRolePolicy;

    @Autowired
    ReactiveRedisTemplate redisTemplate;

    @Test
    @DisplayName("setRoleToPlayer 절차 지향 코드 테스트")
    public void setRoleToPlayer_step() throws Exception {
        //given
        Game game = Game.builder()
                .name("gameName")
                .roomId("roomId")
                .hostId("roomHost")
                .topic(new Topic(1L, "java"))
                .build();

        for (int i = 0; i < 10; i++) {
            game.addPlayer(String.valueOf(i));
        }

        //when
        int random = (int) (Math.random() * game.getPlayerIds().size());
        Flux<JoinPlayer> joinPlayerFlux = Flux.fromIterable(game.getPlayerIds())
                .map(playerId -> {
                    GameRole role = Objects.equals(game.getPlayerIds().get(random), playerId) ? GameRole.LIAR : GameRole.CITIZEN;
                    return new JoinPlayer(game.getRoomId(), new Player(playerId, role));
                });

        //then
        System.out.println("random = " + random);
        joinPlayerFlux.subscribe(f -> System.out.println("userId = " + f.getPlayer().getUserId() + ", role = " + f.getPlayer().getGameRole()));

    }

    @Test
    @DisplayName("setRoleToPlayer Test")
    public void setRoleToPlayer() throws Exception {
        //given
        Game game = Game.builder()
                .name("gameName")
                .roomId("roomId")
                .hostId("roomHost")
                .topic(new Topic(1L, "java"))
                .build();

        for (int i = 0; i < 10; i++) {
            game.addPlayer(String.valueOf(i));
        }

        //when
        Flux<JoinPlayer> joinPlayerFlux = setJoinPlayerRolePolicy.setRoleToPlayer(game);

        //then
        StepVerifier.create(joinPlayerFlux)
                .expectNextMatches(joinPlayer -> joinPlayer.getPlayer().getGameRole() == GameRole.LIAR)
                .expectNextCount(1)
                .expectComplete();

    }

}