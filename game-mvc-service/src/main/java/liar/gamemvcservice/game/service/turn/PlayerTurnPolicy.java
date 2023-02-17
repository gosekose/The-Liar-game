package liar.gamemvcservice.game.service.turn;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameTurn;
import org.springframework.stereotype.Component;

@Component
public interface PlayerTurnPolicy {

    /**
     * 플레이어의 턴을 정하는 메소드
     */
    GameTurn setUpTurn(Game game);

    /**
     * 플레이어의 턴이 맞는지 확인
     * 검증이 되면, 턴의 횟수를 늘린다.
     */
    GameTurn updatePlayerTurnWhenPlayerTurnIsValidated(GameTurn gameTurn, String userId);

    /**
     * 플레이어가 시간 안에 topic 설명을 하지 못하면, 시간초과로 인해 턴을 넘기는 메소드
     */
    GameTurn timeOut(GameTurn gameTurn);
}
