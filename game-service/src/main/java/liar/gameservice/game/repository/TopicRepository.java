package liar.gameservice.game.repository;

import liar.gameservice.game.domain.Topic;
import liar.gameservice.game.service.dao.TopicName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends ReactiveCrudRepository<Topic, Long> {
    TopicName findNameOnlyById(Long id);
}
