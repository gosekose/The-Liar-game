package liar.gameservice.game.service.policy.impl;

import liar.gameservice.game.domain.Topic;
import liar.gameservice.game.repository.CrudValueCustomRepository;
import liar.gameservice.game.service.policy.interfaces.SetTopicPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SetTopicPolicyImpl implements SetTopicPolicy {

    private final CrudValueCustomRepository<Topic, Long> repository;

    @Value("${topic.range}")
    private long range;

    @Override
    public Mono<Topic> setTopic() {
        return Mono.just(generatorId())
                .flatMap(topicId -> repository.get(topicId, Topic.class))
                .filter(Objects::nonNull)
                .switchIfEmpty(setTopic());
    }

    private long generatorId() {
        return ((long)(Math.random() * range)) + 1;
    }
}
