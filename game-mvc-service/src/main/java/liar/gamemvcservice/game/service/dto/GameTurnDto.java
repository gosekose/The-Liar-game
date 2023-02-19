package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.GameTurn;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GameTurnDto {
    private String gameId;
    private List<String> userIdTurns;

    public static GameTurnDto of(GameTurn gameTurn) {
        return new GameTurnDto(gameTurn.getGameId(), gameTurn.getPlayerTurnsConsistingOfUserId());
    }

    public GameTurnDto(String gameId, List<String> userIdTurns) {
        this.gameId = gameId;
        this.userIdTurns = userIdTurns;
    }
}
