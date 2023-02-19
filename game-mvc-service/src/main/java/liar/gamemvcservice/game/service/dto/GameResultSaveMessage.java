package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GameResultSaveMessage {
    private String gameId;
    private String roomId;
    private String gameName;
    private String hostId;
    private Long topicId;
    private int totalUserCnt;
    private GameRole winner;
    private List<PlayersInfoDto> playersInfo;

    protected GameResultSaveMessage(String gameId, String roomId, String gameName, String hostId,
                                 Long topicId, int totalUserCnt, GameRole winner,
                                    List<PlayersInfoDto> playersInfo) {
        this.gameId = gameId;
        this.roomId = roomId;
        this.gameName = gameName;
        this.hostId = hostId;
        this.topicId = topicId;
        this.totalUserCnt = totalUserCnt;
        this.winner = winner;
        this.playersInfo = playersInfo;
    }

    public static GameResultSaveMessage of(Game game, GameResultToClient results) {
        return new GameResultSaveMessage(game.getId(), game.getRoomId(), game.getGameName(),
                game.getHostId(), game.getTopic().getId(), game.getPlayerIds().size(),
                results.getWinner(), results.getPlayersInfo());
    }
}
