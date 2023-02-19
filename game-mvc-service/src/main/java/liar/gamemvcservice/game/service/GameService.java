package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.game.service.dto.CommonDto;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import liar.gamemvcservice.game.controller.dto.VoteLiarRequest;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.GameTurnRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import liar.gamemvcservice.game.service.dto.VoteLiarDto;
import liar.gamemvcservice.game.service.player.PlayerPolicy;
import liar.gamemvcservice.game.service.result.ResultPolicy;
import liar.gamemvcservice.game.service.topic.TopicPolicy;
import liar.gamemvcservice.game.service.turn.PlayerTurnPolicy;
import liar.gamemvcservice.game.service.vote.VotePolicy;
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
    private final VotePolicy votePolicy;
    private final ResultPolicy resultPolicy;
    private final JoinPlayerRepository joinPlayerRepository;
    private final GameTurnRepository gameTurnRepository;

    public String save(SetUpGameDto dto) {
        Game notSetUpTopicGame = Game.of(dto);
        String liarId = playerPolicy.setUpPlayerRole(notSetUpTopicGame);
        Topic topic = topicPolicy.setUp();
        Game completeGame = notSetUpTopicGame.updateTopicOfGame(topic, liarId);
        return gameRepository.save(completeGame).getId();
    }

    public Player checkPlayerRole(CommonDto dto) {
        return playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());
    }

    public Topic checkTopic(CommonDto dto) {
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

    public JoinPlayer findJoinMemberOfRequestGame(String gameId, String userId) {
        return findJoinPlayersByGameId(gameId)
                .stream()
                .filter(player -> player.getPlayer().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(NotFoundGameException::new);
    }

    public List<String> setUpTurn(String gameId) {
        Game game = findGameById(gameId);
        GameTurn gameTurn = playerTurnPolicy.setUpTurn(game);
        return gameTurn.getPlayerTurnsConsistingOfUserId();
    }

    public NextTurn updateAndInformPlayerTurn(String gameId, String userId) throws InterruptedException {
        GameTurn gameTurn = playerTurnPolicy
                .updateTurnWhenPlayerTurnIsValidated(gameTurnRepository.findGameTurnByGameId(gameId), userId);

        NextTurn nextTurn = gameTurn.setIfExistsNextTurn();

        if (nextTurn.getUserIdOfNextTurn() == null) {
            Game game = gameRepository.findById(gameId).orElseThrow(NotFoundGameException::new);
            votePolicy.saveVote(game);
        }

        return nextTurn;
    }

    public void saveVote(VoteLiarDto dto) throws InterruptedException {
        votePolicy.voteLiarUser(dto.getGameId(), dto.getUserId(), dto.getLiarId());
    }

    private List<JoinPlayer> findJoinPlayersByGameId(String gameId) {
        return isValidateGameId(joinPlayerRepository.findByGameId(gameId));
    }

    private List<JoinPlayer> isValidateGameId(List<JoinPlayer> joinPlayers) {
        if (!joinPlayers.isEmpty()) {
            return joinPlayers;
        }
        throw new NotFoundGameException();
    }

}
