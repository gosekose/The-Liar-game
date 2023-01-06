package liar.game.business.game.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class AllMembersGameResultAnalysisDto {

    private Long id;
    private long win;
    private long lose;
    private Double winningRate;
    private LocalDateTime lastJoinGameTime;

    @Builder
    public AllMembersGameResultAnalysisDto(Long id, long win, long lose, Double winningRate, LocalDateTime lastJoinGameTime) {
        this.id = id;
        this.win = win;
        this.lose = lose;
        this.winningRate = winningRate;
        this.lastJoinGameTime = lastJoinGameTime;
    }
}
