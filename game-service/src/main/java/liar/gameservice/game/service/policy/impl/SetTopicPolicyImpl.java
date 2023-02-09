package liar.gameservice.game.service.policy.impl;

import liar.gameservice.game.domain.Topic;
import liar.gameservice.game.repository.CrudValueCustomRepository;
import liar.gameservice.game.service.policy.interfaces.SetTopicPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SetTopicPolicyImpl implements SetTopicPolicy {

    private final CrudValueCustomRepository<Topic, Long> repository;

    @Value("${topic.range}")
    private long range;

    @Override
    public Topic setTopic() {

        Topic topic;
        long topicId = generatorId();
        while ((topic = repository.get(topicId, Topic.class)) == null) topicId = generatorId();

        return topic;
    }

    private long generatorId() {
        return ((long)(Math.random() * range)) + 1;
    }
}
