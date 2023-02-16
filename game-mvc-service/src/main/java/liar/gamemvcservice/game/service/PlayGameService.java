package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.game.controller.ChatMessage;
import liar.gamemvcservice.game.domain.JoinPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayGameService {

    private final GameService gameService;

    public JoinPlayer findJoinMemberOfRequestGame(String gameId, String userId) {
        return gameService.findJoinPlayersByGameId(gameId)
                .stream()
                .filter(player -> player.getId().equals(userId))
                .findFirst()
                .orElseThrow(NotFoundGameException::new);

    }


}
