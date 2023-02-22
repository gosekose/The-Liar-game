package liar.resultservice.result.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "player_result_user_id_index", columnList = "userId")
})
public class PlayerResult extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "player_result_id")
    private AtomicLong id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_result_id")
    private GameResult gameResult;

    private String userId;
    private GameRole gameRole;
    private Boolean answers;
    private Boolean isWin;
    private Long exp;
}
