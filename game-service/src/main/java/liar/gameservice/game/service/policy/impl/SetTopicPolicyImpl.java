package liar.gameservice.game.service.policy.impl;

import liar.gameservice.game.domain.Topic;
import liar.gameservice.game.service.policy.interfaces.SetTopicPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SetTopicPolicyImpl implements SetTopicPolicy {

    private final ReactiveRedisTemplate<String, Topic> reactiveRedisTemplate;

    @Value("${topic.range}")
    private long range;

    @Override
    public Mono<Topic> setTopic() {
        return Mono.just(generatorId())
                .flatMap(topicId -> reactiveRedisTemplate.opsForValue().get("Topic:" + topicId))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.just(new Topic(1000L, "soccer")));
    }

    private long generatorId() {
        return ((long)(Math.random() * range)) + 1;
    }
}
