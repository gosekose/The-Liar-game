package liar.gamemvcservice.game.repository;

import liar.gamemvcservice.game.domain.Topic;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class TopicRepositoryTest {

    @Autowired
    TopicRepository topicRepository;

    @Test
    @DisplayName("topic을 저장한다.")
    public void saveTopic() throws Exception {
        //given
        Topic topic = new Topic("안경");

        //when
        Topic savedTopic = topicRepository.save(topic);

        //then
        assertThat(savedTopic.getTopicName()).isEqualTo(topic.getTopicName());

    }

}