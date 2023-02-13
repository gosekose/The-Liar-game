package liar.gamemvcservice.game.service;

import liar.gamemvcservice.game.domain.Topic;
import liar.gamemvcservice.game.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicPolicyImpl implements TopicPolicy {

    @Value("${topic.range}")
    private long range;
    private final TopicRepository topicRepository;

    private static Topic[] supplyTopic = {new Topic(314000L, "축구"),
            new Topic(314001L, "농구"),
            new Topic(314002L, "배구"),
            new Topic(314003L, "학교"),
            new Topic(314004L, "스마트폰")
    };

    @Override
    public Topic setUp() {
        Topic topic;
        long randomId = (long) (Math.random() * range) + 1;
        int cnt = 0;

        while((topic = topicRepository.findTopicById(randomId)) == null && cnt < 10) {
            randomId = (long) (Math.random() * range) + 1;
            cnt++;
        }

        if (topic == null) {
            topic = supplyTopic[(int) (Math.random() * supplyTopic.length)];
        }

        return topic;
    }
}
