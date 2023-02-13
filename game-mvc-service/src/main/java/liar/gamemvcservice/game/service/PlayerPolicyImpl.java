package liar.gamemvcservice.game.service;

import jakarta.ws.rs.NotFoundException;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameRole;
import liar.gamemvcservice.game.domain.JoinPlayer;
import liar.gamemvcservice.game.domain.Player;
import liar.gamemvcservice.game.repository.JoinPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
public class PlayerPolicyImpl implements PlayerPolicy {

    private final JoinPlayerRepository joinPlayerRepository;

    @Override
    public void setUpPlayerRole(Game game) {

        int randomIdx = (int) (Math.random() * game.getPlayerIds().size());
        String userId = game.getPlayerIds().get(randomIdx);

        if (userId == null) {
            throw new NotFoundException();
        }

        game.getPlayerIds().stream()
                .forEach(
                        f -> {
                            if (f.equals(userId)) {
                                joinPlayerRepository.save(new JoinPlayer(game.getId(),
                                        new Player(userId, GameRole.LIAR)));
                            } else {
                                joinPlayerRepository.save(new JoinPlayer(game.getId(),
                                        new Player(userId, GameRole.CITIZEN)));
                            }
                        }
                );
    }

    @Override
    public Player checkPlayerInfo(String gameId, String userId) {
        List<JoinPlayer> joinPlayers = joinPlayerRepository.findByGameId(gameId);

        return joinPlayers.stream()
                .filter(f -> f.getPlayer().getUserId().equals(userId))
                .map(JoinPlayer::getPlayer)
                .findFirst()
                .orElseThrow(NotFoundUserException::new);
    }
}
