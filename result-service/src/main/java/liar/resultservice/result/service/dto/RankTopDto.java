package liar.resultservice.result.service.dto;

import liar.resultservice.result.domain.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankTopDto {
    private String userId;
    private String userName;
    private Level level;
    private Long exp;
    private Long wins;
    private Long loses;
    private Long totalGames;
}
