package liar.game.business.game.domain;

import liar.game.common.BaseEntity;
import liar.game.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "game_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member host;

    public Game(Member host) {
        this.host = host;
    }

    public static Game of (Member host) {
        return new Game(host);
    }
}
