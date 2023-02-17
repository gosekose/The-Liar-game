package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.game.controller.dto.RequestCommonDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.GameTurnRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
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
    private final JoinPlayerRepository joinPlayerRepository;
    private final GameTurnRepository gameTurnRepository;

    public String save(SetUpGameDto dto) {
        Game notSetUpTopicGame = Game.of(dto);
        Topic topic = topicPolicy.setUp();
        Game setUpTopicGame = notSetUpTopicGame.updateTopicOfGame(topic);
        playerPolicy.setUpPlayerRole(setUpTopicGame);
        return gameRepository.save(setUpTopicGame).getId();
    }

    public Player checkPlayerRole(RequestCommonDto dto) {
        return playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());
    }

    public Topic checkTopic(RequestCommonDto dto) {
        Player player = playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());

        if (player.getGameRole() == GameRole.CITIZEN) {
            Game game = findGameById(dto.getGameId());
            return game.getTopic();
        }

        return null;
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

    public JoinPlayer findJoinMemberOfRequestGame(String gameId, String userId) {
        return findJoinPlayersByGameId(gameId)
                .stream()
                .filter(player -> player.getId().equals(userId))
                .findFirst()
                .orElseThrow(NotFoundGameException::new);
    }

    public List<String> setUpTurn(String gameId) {
        Game game = findGameById(gameId);
        GameTurn gameTurn = playerTurnPolicy.setUpTurn(game);
        return gameTurn.getPlayerTurnsConsistingOfUserId();
    }

    public NextTurn updatePlayerTurnAndNotifyNextTurnWhenPlayerTurnIsValidated(String gameId, String userId) {
        GameTurn gameTurn = playerTurnPolicy
                .updatePlayerTurnWhenPlayerTurnIsValidated(gameTurnRepository.findGameTurnByGameId(gameId), userId);
        gameTurnRepository.save(gameTurn);
        return gameTurn.setIfExistsNextTurn();
    }

}
