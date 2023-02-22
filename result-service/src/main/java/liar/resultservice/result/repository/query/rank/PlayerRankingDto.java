package liar.resultservice.result.repository.query.rank;

import com.querydsl.core.annotations.QueryProjection;
import liar.resultservice.result.domain.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerRankingDto {
    private String userId;
    private String userName;
    private Level level;
    private Long exp;
    private Long wins;
    private Long loses;
    private Long totalGames;

    @QueryProjection
    public PlayerRankingDto(String userId, String userName, Level level, Long exp, Long wins, Long loses, Long totalGames) {
        this.userId = userId;
        this.userName = userName;
        this.level = level;
        this.exp = exp;
        this.wins = wins;
        this.loses = loses;
        this.totalGames = totalGames;
    }
}
