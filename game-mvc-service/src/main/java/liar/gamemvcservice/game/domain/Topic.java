package liar.gamemvcservice.game.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic {

    @Id @GeneratedValue
    @Column(name = "topic_id")
    private Long id;

    private String topicName;

    public Topic(String topicName) {
        this.topicName = topicName;
    }

    public Topic(Long id, String topicName) {
        this.id = id;
        this.topicName = topicName;
    }
}
