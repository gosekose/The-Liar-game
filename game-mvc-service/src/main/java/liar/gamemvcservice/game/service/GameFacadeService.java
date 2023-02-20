package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import liar.gamemvcservice.game.service.dto.CommonDto;
import liar.gamemvcservice.game.service.dto.GameResultToClientDto;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
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
public class GameFacadeService {

    private final GameRepository gameRepository;
    private final TopicPolicy topicPolicy;
    private final PlayerPolicy playerPolicy;
    private final PlayerTurnPolicy playerTurnPolicy;
    private final VotePolicy votePolicy;
    private final ResultPolicy resultPolicy;
    private final JoinPlayerRepository joinPlayerRepository;
    private final GameTurnRepository gameTurnRepository;
    private final VoteRepository voteRepository;

    /**
     * 방장의 요청을 받아 game을 저장한다,
     * @param dto game 설정 정보
     * @return gameId
     */
    public String save(SetUpGameDto dto) {
        Game notSetUpTopicGame = Game.of(dto);
        String liarId = playerPolicy.setUpPlayerRole(notSetUpTopicGame);
        Topic topic = topicPolicy.setUp();
        Game completeGame = notSetUpTopicGame.updateTopicOfGame(topic, liarId);
        return gameRepository.save(completeGame).getId();
    }

    /**
     * 플레이어의 역할을 조회한다.
     * @param dto gameId, userId
     * @return player 객체
     */
    public Player checkPlayerRole(CommonDto dto) {
        return playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());
    }

    /**
     * 클라이언트의 role이 CITIZEN이면, topic을 반환한다.
     * @param dto gameId, userId
     * @return topic (게임 설명 주제)
     */
    public Topic checkTopic(CommonDto dto) {
        Player player = playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());

        if (player.getGameRole() == GameRole.CITIZEN) {
            Game game = findGameById(dto.getGameId());
            return game.getTopic();
        }
        return null;
    }

    /**
     * gameId, userId로 joinPlayer 값을 조회한다.
     * @return joinPlayer
     */
    public JoinPlayer findJoinPlayer(String gameId, String userId) {
        return findJoinPlayersByGameId(gameId)
                .stream()
                .filter(player -> player.getPlayer().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(NotFoundGameException::new);
    }

    /**
     * gameId를 받아, 게임의 턴을 설정한다.
     * @param gameId gameId
     * @return gameTurns(userId로 구성)
     */
    public List<String> setUpTurn(String gameId) {
        Game game = findGameById(gameId);
        GameTurn gameTurn = playerTurnPolicy.setUpTurn(game);
        return gameTurn.getPlayerTurnsConsistingOfUserId();
    }

    /**
     * 플레이어의 턴을 업데이트하고, 마지막 턴이라면 턴의 결과를 알리며, vote를 초기화하여 저장한다.
     * @return nextTurn (다음턴 userId, 마지막 턴 boolean)
     * @throws InterruptedException
     */
    public NextTurn updateAndInformPlayerTurn(String gameId, String userId) throws InterruptedException {
        GameTurn gameTurn = playerTurnPolicy
                .updateTurnWhenPlayerTurnIsValidated(gameTurnRepository.findGameTurnByGameId(gameId), userId);

        NextTurn nextTurn = gameTurn.setIfExistsNextTurn();
        saveVoteAtLastTurnEnd(gameId, nextTurn);

        return nextTurn;
    }

    /**
     * 클라이언트의 개별 투표를 저장한다.
     * gameId(게임 Id), userId(클라이언트의 userId), liarId(라이어로 지목할 클라이언트 userId)
     * @return vote가 수정되어 저장되면 true, 아니라면 false
     * @throws InterruptedException
     */
    public boolean voteLiarUser(String gameId, String userid, String liarId) throws InterruptedException {
        return votePolicy.voteLiarUser(gameId, userid, liarId);
    }

    /**
     * gameResult를 client에게 전달하는 Dto를 생성한다.
     * @param gameId gameId
     * @return gameResultToClientDto
     */
    public GameResultToClientDto informGameResult(String gameId) {
        Game game = findGameById(gameId);
        Vote vote = getVote(game);
        return resultPolicy.informGameResult(game, vote.getVotedResults());
    }

    /**
     * 게임의 마지막 턴인 경우, vote 초기화 값을 저장한다.
     * -> updateAndInformPlayerTurn
     * @param gameId gameId
     * @param nextTurn nextTurn
     * @throws InterruptedException
     */
    private void saveVoteAtLastTurnEnd(String gameId, NextTurn nextTurn) throws InterruptedException {
        if (nextTurn.getUserIdOfNextTurn() == null) {
            Game game = findGameById(gameId);
            votePolicy.saveVote(game);
        }
    }

    /**
     * gameId로 Game을 찾는다.
     * @param gameId
     * @return game
     */
    private Game findGameById(String gameId) {
        return gameRepository.findById(gameId).orElseThrow(NotFoundGameException::new);
    }

    /**
     * gameId로 JoinPlayers 찾기
     */
    private List<JoinPlayer> findJoinPlayersByGameId(String gameId) {
        return isValidateGameId(joinPlayerRepository.findByGameId(gameId));
    }

    /**
     * joinPlayers 리턴, empty라면 exception
     */
    private List<JoinPlayer> isValidateGameId(List<JoinPlayer> joinPlayers) {
        if (!joinPlayers.isEmpty()) {
            return joinPlayers;
        }
        throw new NotFoundGameException();
    }

    private Vote getVote(Game game) {
        Vote vote = voteRepository.findVoteByGameId(game.getId());
        if (vote == null) throw new NotFoundGameException();
        return vote;
    }

}
