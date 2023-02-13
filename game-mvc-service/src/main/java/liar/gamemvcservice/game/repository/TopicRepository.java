package liar.gamemvcservice.game.repository;

import liar.gamemvcservice.game.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findTopicById(Long id);
}
