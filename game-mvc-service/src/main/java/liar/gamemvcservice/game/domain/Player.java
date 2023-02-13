package liar.gamemvcservice.game.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    private String userId;
    private GameRole gameRole;

    public void updateGameRole(GameRole gameRole) {
        this.gameRole = gameRole;
    }

}
