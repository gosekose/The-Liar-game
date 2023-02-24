package liar.resultservice.result.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "player_result",
        indexes = {
        @Index(name = "player_result_user_id_index", columnList = "userId")
})
public class PlayerResult extends BaseEntity implements Persistable<String>  {

    @Id
    @Column(name = "player_result_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_result_id")
    private GameResult gameResult;

    private String userId;
    private GameRole gameRole;
    private Boolean answers;
    private Boolean isWin;
    private Long exp;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return super.getCreatedAt() == null;
    }

    @Builder
    public PlayerResult(GameResult gameResult, String userId, GameRole gameRole, Boolean answers, Boolean isWin, Long exp) {
        this.id = UUID.randomUUID().toString();
        this.gameResult = gameResult;
        this.userId = userId;
        this.gameRole = gameRole;
        this.answers = answers;
        this.isWin = isWin;
        this.exp = exp;
    }
}
