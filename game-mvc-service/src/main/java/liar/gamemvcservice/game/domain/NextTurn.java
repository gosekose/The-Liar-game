package liar.gamemvcservice.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NextTurn {

    private String userIdOfNextTurn;
    private boolean isLastTurn;

}