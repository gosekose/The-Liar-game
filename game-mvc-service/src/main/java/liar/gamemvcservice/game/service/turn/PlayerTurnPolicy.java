package liar.gamemvcservice.game.service.turn;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameTurn;
import org.springframework.stereotype.Component;

@Component
public interface PlayerTurnPolicy {

    GameTurn setUpTurn(Game game);

    boolean isFirstSetUpTurn(Game game);

}
