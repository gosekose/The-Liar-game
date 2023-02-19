package liar.gamemvcservice.game.service.turn;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameTurn;
import org.springframework.stereotype.Component;

@Component
public interface PlayerTurnPolicy {

    /**
     * 플레이어의 턴을 정한다.
     */
    GameTurn setUpTurn(Game game);

    /**
     * 플레이어의 턴이 맞는지 확인
     * 검증이 되면, 턴의 횟수를 늘린다.
     * 턴이 아니라면 예외를 발생시킨다.
     */
    GameTurn updateTurnWhenPlayerTurnIsValidated(GameTurn gameTurn, String userId);

    /**
     * 플레이어가 시간 안에 턴을 마치지 못하면, 시간초과로 인해 턴을 넘긴다.
     */
    GameTurn timeOut(GameTurn gameTurn);
}
