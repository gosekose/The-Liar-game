package liar.resultservice.result.domain;

import jakarta.persistence.*;
import liar.resultservice.other.topic.Topic;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "game_result",
        indexes = {
        @Index(name = "game_result_game_id_index", columnList = "gameId")
})
public class GameResult extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "game_result_id")
    private String id;

    private String gameId;
    private String roomId;
    private String hostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private String gameName;
    private GameRole winner;
    private int totalUsers;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return super.getCreatedAt() == null;
    }

    @Builder
    public GameResult(String gameId, String roomId, String hostId, Topic topic, String gameName, GameRole winner, int totalUsers) {
        this.id = UUID.randomUUID().toString();
        this.gameId = gameId;
        this.roomId = roomId;
        this.hostId = hostId;
        this.topic = topic;
        this.gameName = gameName;
        this.winner = winner;
        this.totalUsers = totalUsers;
    }
}
