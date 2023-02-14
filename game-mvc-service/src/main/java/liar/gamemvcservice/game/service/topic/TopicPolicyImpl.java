package liar.gamemvcservice.game.service.topic;

import liar.gamemvcservice.game.domain.Topic;
import liar.gamemvcservice.game.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class TopicPolicyImpl implements TopicPolicy {

    @Value("${topic.range}")
    private long range;
    private final TopicRepository topicRepository;

    private static Topic[] supplyTopic = {
            new Topic(314000L, "축구"),
            new Topic(314001L, "농구"),
            new Topic(314002L, "배구"),
            new Topic(314003L, "학교"),
            new Topic(314004L, "스마트폰"),
            new Topic(314005L, "초콜릿"),
            new Topic(314006L, "연예인"),
            new Topic(314007L, "유재석")
    };

    @Override
    public Topic setUp() {
        Topic topic;
        long randomId = (long) (Math.random() * range) + 1;
        int cnt = 0;

        while((topic = topicRepository.findTopicById(randomId)) == null && cnt < 3) {
            randomId = (long) (Math.random() * range) + 1;
            cnt++;
        }

        if (topic == null) {
            topic = supplyTopic[(int) (Math.random() * supplyTopic.length)];
        }

        return topic;
    }
}
