package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.GameRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameResultToClientDto extends GameResultBaseDto {
    private List<VotedResultDto> votedResults;

    protected GameResultToClientDto(String gameId, GameRole winner, List<VotedResultDto> votedResults,
                                    List<PlayerResultInfoDto> playersInfo) {
        super(gameId, winner, playersInfo);
        this.votedResults = votedResults;
    }

    public static GameResultToClientDto fromBaseDtoAndVoteResults(GameResultBaseDto baseDto, List<VotedResultDto> votedResults) {
        return new GameResultToClientDto(baseDto.getGameId(), baseDto.getWinner(),
                votedResults, baseDto.getPlayersInfo());
    }

}
