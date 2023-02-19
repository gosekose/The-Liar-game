package liar.gamemvcservice.game.service.turn;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameTurn;
import liar.gamemvcservice.game.repository.redis.GameTurnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class PlayerTurnPolicyImpl implements PlayerTurnPolicy {

    private final GameTurnRepository gameTurnRepository;

    /**
     * 플레이어의 턴을 정한다.
     */
    @Override
    public GameTurn setUpTurn(Game game) {
        if (isFirstSetUpTurn(game)) {
            GameTurn gameTurn = new GameTurn(game.getId(), game.shufflePlayer());
            return gameTurnRepository.save(gameTurn);
        }
        return gameTurnRepository.findGameTurnByGameId(game.getId());
    }

    /**
     * 플레이어의 턴이 맞는지 확인
     * 검증이 되면, 턴의 횟수를 늘린다.
     * 턴이 아니라면 예외를 발생시킨다.
     */
    @Override
    public GameTurn updateTurnWhenPlayerTurnIsValidated(GameTurn gameTurn, String userId) {
        return gameTurnRepository.save(gameTurn.updateTurnCntWhenPlayerTurnIsValidated(userId));
    }

    /**
     * 플레이어가 시간 안에 턴을 마치지 못하면, 시간초과로 인해 턴을 넘긴다.
     */
    @Override
    public GameTurn timeOut(GameTurn gameTurn) {
        return gameTurn.updateTurnCntByTimeOut();
    }

    /**
     * Turn을 setUp하는 요청이 처음인지 확인.
     */
    private boolean isFirstSetUpTurn(Game game) {
        GameTurn gameTurn = gameTurnRepository.findGameTurnByGameId(game.getId());
        if (gameTurn == null) {
            return true;
        }
        return false;
    }

}
