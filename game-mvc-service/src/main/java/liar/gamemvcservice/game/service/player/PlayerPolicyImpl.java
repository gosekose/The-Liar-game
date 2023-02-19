package liar.gamemvcservice.game.service.player;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameRole;
import liar.gamemvcservice.game.domain.JoinPlayer;
import liar.gamemvcservice.game.domain.Player;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class PlayerPolicyImpl implements PlayerPolicy {

    private final JoinPlayerRepository joinPlayerRepository;

    @Override
    public String setUpPlayerRole(Game game) {

        int randomIdx = (int) (Math.random() * game.getPlayerIds().size());
        String liarId = game.getPlayerIds().get(randomIdx);

        if (liarId == null) {
            throw new NotFoundUserException();
        }

        game.getPlayerIds().stream()
                .forEach(
                        userId -> {
                            if (userId.equals(liarId)) {
                                joinPlayerRepository.save(new JoinPlayer(game.getId(),
                                        new Player(userId, GameRole.LIAR)));
                            } else {
                                joinPlayerRepository.save(new JoinPlayer(game.getId(),
                                        new Player(userId, GameRole.CITIZEN)));
                            }
                        }
                );

        return liarId;
    }

    @Override
    public Player checkPlayerInfo(String gameId, String userId) {
        List<JoinPlayer> joinPlayers = joinPlayerRepository.findByGameId(gameId);

        if (joinPlayers.isEmpty()) {
            throw new NotFoundGameException();
        }

        return joinPlayers.stream()
                .filter(f -> f.getPlayer().getUserId().equals(userId))
                .map(JoinPlayer::getPlayer)
                .findFirst()
                .orElseThrow(NotFoundUserException::new);
    }
}
