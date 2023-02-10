package liar.gameservice.game.service;

import liar.gameservice.game.domain.Game;
import liar.gameservice.game.domain.JoinPlayer;
import liar.gameservice.game.domain.Topic;
import liar.gameservice.game.service.policy.interfaces.SetJoinPlayerRolePolicy;
import liar.gameservice.game.service.policy.interfaces.SetTopicPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SetGameInitService {

    private final SetJoinPlayerRolePolicy setJoinPlayerRolePolicy;
    private final SetTopicPolicy setTopicPolicy;

}
