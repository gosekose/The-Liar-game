package liar.gameservice.game.service.policy.interfaces;

import liar.gameservice.game.domain.Topic;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
public interface SetTopicPolicy {

    Mono<Topic> setTopic();
}
