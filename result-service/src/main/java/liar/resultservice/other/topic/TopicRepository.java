package liar.resultservice.other.topic;

import liar.resultservice.other.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface TopicRepository extends JpaRepository<Topic, AtomicLong> {

}
