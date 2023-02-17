package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.GameTurn;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GameTurnResponse {
    private String gameId;
    private List<String> userIdTurns;

    public static GameTurnResponse of(GameTurn gameTurn) {
        return new GameTurnResponse(gameTurn.getGameId(), gameTurn.getPlayerTurnsConsistingOfUserId());
    }

    public GameTurnResponse(String gameId, List<String> userIdTurns) {
        this.gameId = gameId;
        this.userIdTurns = userIdTurns;
    }
}
