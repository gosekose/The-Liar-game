package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.game.controller.dto.GameUserInfoDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameTurnDto;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.GameRepository;
import liar.gamemvcservice.game.repository.GameTurnRepository;
import liar.gamemvcservice.game.repository.JoinPlayerRepository;
import liar.gamemvcservice.game.service.dto.GameTurnResponse;
import liar.gamemvcservice.game.service.player.PlayerPolicy;
import liar.gamemvcservice.game.service.topic.TopicPolicy;
import liar.gamemvcservice.game.service.turn.PlayerTurnPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final TopicPolicy topicPolicy;
    private final PlayerPolicy playerPolicy;
    private final PlayerTurnPolicy playerTurnPolicy;
    private final GameTurnRepository gameTurnRepository;
    private final JoinPlayerRepository joinPlayerRepository;

    public String save(SetUpGameDto dto) {
        Game notSetUpTopicGame = Game.of(dto);
        Topic topic = topicPolicy.setUp();
        Game setUpTopicGame = notSetUpTopicGame.updateTopicOfGame(topic);
        playerPolicy.setUpPlayerRole(setUpTopicGame);
        return gameRepository.save(setUpTopicGame).getId();
    }

    public Player checkPlayerRole(GameUserInfoDto dto) {
        return playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());
    }

    public Topic checkTopic(GameUserInfoDto dto) {
        Player player = playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());

        if (player.getGameRole() == GameRole.CITIZEN) {
            Game game = findGameById(dto.getGameId());
            return game.getTopic();
        }

        return null;
    }

    public GameTurnResponse setUpTurn(SetUpGameTurnDto dto) {
        Game game = findGameById(dto.getGameId());
        return GameTurnResponse.of(playerTurnPolicy.setUpTurn(game));
    }

    public Game findGameById(String gameId) {
        return gameRepository.findById(gameId).orElseThrow(NotFoundGameException::new);
    }

    public List<JoinPlayer> findJoinPlayersByGameId(String gameId) {
        return isValidateGameId(joinPlayerRepository.findByGameId(gameId));
    }

    private List<JoinPlayer> isValidateGameId(List<JoinPlayer> joinPlayers) {
        if (!joinPlayers.isEmpty()) {
            return joinPlayers;
        }
        throw new NotFoundGameException();
    }

}
