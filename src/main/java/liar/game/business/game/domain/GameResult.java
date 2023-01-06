package liar.game.business.game.domain;

import liar.game.common.BaseEntity;
import liar.game.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gameresult")
public class GameResult extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "game_result_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "join_member_id")
    private Member member;

    @Enumerated(STRING)
    private Result result;

    @Builder
    public GameResult(Game game, Member member, Result result) {
        this.game = game;
        this.member = member;
        this.result = result;
    }
}
