package liar.resultservice.other.topic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TopicRepositoryTest {

    @Autowired
    TopicRepository topicRepository;

    @Test
    @DisplayName("topic 저장")
    public void save() throws Exception {
        //given
        Topic topic = topicRepository.save(new Topic("game"));

        Assertions.assertThat(topic.getTopicName()).isEqualTo("game");
    }

}