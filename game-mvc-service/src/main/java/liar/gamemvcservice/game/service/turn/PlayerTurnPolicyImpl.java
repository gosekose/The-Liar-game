package liar.gamemvcservice.game.service.turn;

import com.sun.jdi.request.DuplicateRequestException;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameTurn;
import liar.gamemvcservice.game.repository.GameTurnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class PlayerTurnPolicyImpl implements PlayerTurnPolicy {

    private final GameTurnRepository gameTurnRepository;

    @Override
    public GameTurn setUpTurn(Game game) {
        if (isFirstSetUpTurn(game)) {
            GameTurn gameTurn = new GameTurn(game.getId(), game.shufflePlayer());
            return gameTurnRepository.save(gameTurn);
        }
        throw new DuplicateRequestException();
    }

    @Override
    public boolean isFirstSetUpTurn(Game game) {
        GameTurn gameTurn = gameTurnRepository.findGameTurnByGameId(game.getId());
        if (gameTurn == null) {
            return true;
        }
        return false;
    }
}
