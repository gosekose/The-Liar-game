package liar.gamemvcservice.game.service;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.Player;
import org.springframework.stereotype.Component;

@Component
public interface PlayerPolicy {
    void setUpPlayerRole(Game game);

    Player checkPlayerInfo(String gameId, String userId);
}
