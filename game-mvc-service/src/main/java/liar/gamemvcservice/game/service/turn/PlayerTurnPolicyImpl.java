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
     * 플레이어의 턴을 정하는 메소드
     *
     * @param game
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
     * 플레이어의 턴이 맞는지 확인하는 메소드
     *
     * @param gameTurn
     * @param userId
     */
    @Override
    public GameTurn updatePlayerTurnWhenPlayerTurnIsValidated(GameTurn gameTurn, String userId) {
        return gameTurn.updateTurnCntWhenPlayerTurnIsValidated(userId);
    }

    @Override
    public GameTurn timeOut(GameTurn gameTurn) {
        return gameTurn.updateTurnCntByTimeOut();
    }

    /**
     * 마감 회전 횟수에 도달하면, 더 이상 턴이 돌지 않도록 마지막임을 알리는 메세지를 전송하는 메소드
     *
     * @param gameTurn
     */
    @Override
    public GameTurn notifyWhenLastTurn(GameTurn gameTurn) {
        return null;
    }

    private boolean isFirstSetUpTurn(Game game) {
        GameTurn gameTurn = gameTurnRepository.findGameTurnByGameId(game.getId());
        if (gameTurn == null) {
            return true;
        }
        return false;
    }

}
