package liar.resultservice.result.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    @Id @GeneratedValue
    @Column(name = "player_id")
    private AtomicLong id;

    private String userId;

    private Long wins;
    private Long loses;
    private Long totalGames;
    private Long exp;
    private Level level;

    private boolean visibleGameResult;

    @Builder
    public Player(String userId, Long wins, Long loses, Long totalGames, Long exp, Level level) {
        this.userId = userId;
        this.wins = wins;
        this.loses = loses;
        this.totalGames = totalGames;
        this.exp = exp;
        this.level = level;
        this.visibleGameResult = true;
    }

    public static Player of(String userId) {
        return Player.builder()
                .userId(userId)
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
