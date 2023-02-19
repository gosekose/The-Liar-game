package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.GameRole;
import liar.gamemvcservice.game.domain.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameResultToClient {
    private String gameId;
    private GameRole winner;
    private ConcurrentHashMap<Player, Boolean> playersInfoAndWhoRightAnswers;

    public static GameResultToClient of(String gameId, GameRole whoWin, ConcurrentHashMap<Player, Boolean> playersInfoAndWhoRightAnswers) {
        return new GameResultToClient(gameId, whoWin, playersInfoAndWhoRightAnswers);
    }
}
