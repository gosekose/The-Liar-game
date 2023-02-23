package liar.resultservice.result.domain;

import jakarta.persistence.*;
import liar.resultservice.other.topic.Topic;
import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "game_result_game_id_index", columnList = "gameId")
})
public class GameResult extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "game_result_id")
    private AtomicLong id;

    private String gameId;
    private String roomId;
    private String hostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private String gameName;
    private GameRole winner;
    private int totalUsers;

    @Builder
    public GameResult(String gameId, String roomId, String hostId, Topic topic, String gameName, GameRole winner, int totalUsers) {
        this.gameId = gameId;
        this.roomId = roomId;
        this.hostId = hostId;
        this.topic = topic;
        this.gameName = gameName;
        this.winner = winner;
        this.totalUsers = totalUsers;
    }
}
