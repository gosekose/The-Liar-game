package liar.resultservice.result.domain;

import jakarta.persistence.*;
import liar.resultservice.other.member.Member;
import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "exp_index", columnList = "exp")
})
public class Player extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "player_id")
    private AtomicLong id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long wins;
    private Long loses;
    private Long totalGames;

    private Long exp;
    private Level level;

    private boolean visibleGameResult;

    @Builder
    public Player(Member member, Long wins, Long loses, Long totalGames, Long exp, Level level) {
        this.member = member;
        this.wins = wins;
        this.loses = loses;
        this.totalGames = totalGames;
        this.exp = exp;
        this.level = level;
        this.visibleGameResult = true;
    }

    public static Player of(Member member) {
        return Player.builder()
                .member(member)
                .wins(0L)
                .loses(0L)
                .totalGames(0L)
                .exp(0L)
                .level(Level.BRONZE1)
                .build();
    }

    public Long updateExp(Long exp) {
        return this.exp += exp;
    }

    public Level levelUp(Level level) {
        return this.level = level;
    }

    public long updateGameResult(boolean win) {
        if (win) wins++; else loses++;
        return ++totalGames;
    }

    public boolean updateVisibleGameResult() {
        if (isVisibleGameResult()) return this.visibleGameResult = false;
        return this.visibleGameResult = true;
    }

}
