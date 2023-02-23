package liar.resultservice.other.topic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    @Id @GeneratedValue
    @Column(name = "topic_id")
    private Long id;

    private String topicName;

    public Topic(String topicName) {
        this.topicName = topicName;
    }
}
