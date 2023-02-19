package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameRole;
import liar.gamemvcservice.game.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

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
    private ConcurrentHashMap<Player, Boolean> playersInfoAndWhoRightAnswers;

    protected GameResultSaveMessage(String gameId, String roomId, String gameName, String hostId,
                                 Long topicId, int totalUserCnt, GameRole winner,
                                 ConcurrentHashMap<Player, Boolean> playersInfoAndWhoRightAnswers) {
        this.gameId = gameId;
        this.roomId = roomId;
        this.gameName = gameName;
        this.hostId = hostId;
        this.topicId = topicId;
        this.totalUserCnt = totalUserCnt;
        this.winner = winner;
        this.playersInfoAndWhoRightAnswers = playersInfoAndWhoRightAnswers;
    }

    public static GameResultSaveMessage of(Game game, GameResultToClient results) {
        return new GameResultSaveMessage(game.getId(), game.getRoomId(), game.getGameName(),
                game.getHostId(), game.getTopic().getId(), game.getPlayerIds().size(),
                results.getWinner(), results.getPlayersInfoAndWhoRightAnswers());
    }
}
