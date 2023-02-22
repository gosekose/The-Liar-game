package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GameResultToServerDto extends GameResultBaseDto{
    private String roomId;
    private String gameName;
    private String hostId;
    private Long topicId;
    private int totalUserCnt;
    private List<VotedResultDto> votedResults;

    protected GameResultToServerDto(String gameId, String roomId, String gameName, String hostId,
                                    Long topicId, int totalUserCnt, GameRole winner,
                                    List<PlayerResultInfoDto> playersInfo, List<VotedResultDto> votedResults
                                    ) {
        super(gameId, winner, playersInfo);
        this.roomId = roomId;
        this.gameName = gameName;
        this.hostId = hostId;
        this.topicId = topicId;
        this.totalUserCnt = totalUserCnt;
        this.votedResults = votedResults;
    }

    public static GameResultToServerDto fromBaseDtoAndGame(GameResultBaseDto baseDto,
                                                           Game game, List<VotedResultDto> votedResults) {
        return new GameResultToServerDto(baseDto.getGameId(),
                game.getRoomId(), game.getGameName(), game.getHostId(),
                game.getTopic().getId(), game.getPlayerIds().size(),
                baseDto.getWinner(), baseDto.getPlayersInfo(), votedResults);
    }

}
