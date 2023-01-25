package liar.game.business.game.domain;

import jakarta.persistence.*;
import liar.game.common.BaseEntity;
import liar.game.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseEntity {

    @Id
    @GeneratedValue
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
