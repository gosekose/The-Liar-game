package liar.gamemvcservice.game.service.topic;

import liar.gamemvcservice.game.domain.Topic;
import liar.gamemvcservice.game.repository.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TopicPolicyTest {

    @Autowired
    TopicPolicy topicPolicy;

    @Autowired
    TopicRepository topicRepository;

    @Test
    @DisplayName("setUp 메소드를 실행하면, 랜덤 id가 db에 있다면 무작위 topic을 가져온다.")
    public void setUp_success() throws Exception {
        //given
        for (int i = 0; i < 13; i++) {
            topicRepository.save(new Topic(String.valueOf(i)));
        }

        //when
        Topic topic = topicPolicy.setUp();

        //then
        assertThat(topic.getTopicName()).isNotNull();
        assertThat(topic.getId()).isNotNull();
    }

    @Test
    @DisplayName("setUp 메소드를 실행할 때, 랜덤 Id에 해당 하는 값들이 없다면, supplyTopic에서 무작위 값을 가져온다")
    public void setUp_supplyTopic_success() throws Exception {
        //given
        Topic topic = topicPolicy.setUp();

        //when
        System.out.println("topic.getId() = " + topic.getId());
        System.out.println("topic.getTopicName() = " + topic.getTopicName());

        //then
        assertThat(topic.getId()).isNotNull();
        assertThat(topic.getId()).isGreaterThan(10L);
        assertThat(topic.getTopicName()).isNotNull();
    }

}