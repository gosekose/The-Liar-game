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
    private Long exp;
}
