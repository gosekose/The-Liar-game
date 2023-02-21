package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.exception.exception.NotFoundVoteException;
import liar.gamemvcservice.game.controller.dto.request.VoteLiarRequest;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import liar.gamemvcservice.game.service.dto.*;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.GameTurnRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import liar.gamemvcservice.game.service.player.PlayerPolicy;
import liar.gamemvcservice.game.service.result.ResultPolicy;
import liar.gamemvcservice.game.service.topic.TopicPolicy;
import liar.gamemvcservice.game.service.turn.PlayerTurnPolicy;
import liar.gamemvcservice.game.service.vote.VotePolicy;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static liar.gamemvcservice.game.domain.GameRole.CITIZEN;
import static liar.gamemvcservice.game.domain.GameRole.LIAR;

@Service
@Transactional
@RequiredArgsConstructor
public class GameFacadeServiceImpl implements GameFacadeService {

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
    @Override
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
    @Override
    public Player checkPlayerRole(CommonDto dto) {
        return playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());
    }


    /**
     * 클라이언트의 role이 CITIZEN이면, topic을 반환한다.
     * @param dto gameId, userId
     * @return topic (게임 설명 주제)
     */
    @Override
    public Topic checkTopic(CommonDto dto) {
        if (playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId()).getGameRole() == GameRole.CITIZEN)
            return findGameByGameId(dto.getGameId()).getTopic();
        return null;
    }


    /**
     * gameId, userId로 joinPlayer 값을 조회한다.
     * @return joinPlayer
     */
    @Override
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
    @Override
    public List<String> setUpTurn(String gameId) {
        return playerTurnPolicy.setUpTurn(findGameByGameId(gameId)).getPlayerTurnsConsistingOfUserId();
    }


    /**
     * 플레이어의 턴을 업데이트하고, 마지막 턴이라면 턴의 결과를 알리며, vote를 초기화하여 저장한다.
     * @return nextTurn (다음턴 userId, 마지막 턴 boolean)
     * @throws InterruptedException
     */
    @Override
    public NextTurn setNextTurnWhenValidated(String gameId, String userId) throws InterruptedException {
        NextTurn nextTurn = setNextTurnIfExistsNextTurn(gameId, userId);
        saveVoteWhenLastTurnEnd(gameId, nextTurn);
        return nextTurn;
    }


    /**
     * 클라이언트의 개별 투표를 저장한다.
     * gameId(게임 Id), userId(클라이언트의 userId), liarId(라이어로 지목할 클라이언트 userId)
     * @return vote가 수정되어 저장되면 true, 아니라면 false
     * @throws InterruptedException
     */
    @Override
    public boolean voteLiarUser(VoteLiarRequest dto) throws InterruptedException {
        return votePolicy.voteLiarUser(dto.getGameId(), dto.getUserId(), dto.getLiarId());
    }


    /**
     * gameResult를 client에게 전달하는 Dto를 생성한다.
     * @param gameId
     * @return gameResultToClientDto
     */
    @Override
    public GameResultToClientDto sendGameResultToClient(String gameId) {
        Game game = findGameByGameId(gameId);
        return createGameResultToClientDto(game, getVote(game));
    }

    /**
     * 같은 게임을 공유하는 gameResultToServer가 처음 실행 되면 game save message를 보낸다.
     * @param gameId
     * @return gameResultToServerDto
     */
    @Override
    public GameResultToServerDto sendGameResultToServer(String gameId) {
        Game game = findGameByGameId(gameId);
        if (!game.isSendMessage()) return createGameResultToServerDto(game);
        return null;
    }

    @NotNull
    private GameResultToClientDto createGameResultToClientDto(Game game, Vote vote) {
        return GameResultToClientDto
                .fromBaseDtoAndVoteResults(
                        createGameResultBaseDto(game, vote), resultPolicy.getVotedResultDto(vote));
    }

    @NotNull
    private GameResultToServerDto createGameResultToServerDto(Game game) {
        Vote vote = getVote(game);
        updateSendMessage(game);
        return GameResultToServerDto
                .fromBaseDtoAndGame(createGameResultBaseDto(game, vote), game, resultPolicy.getVotedResultDto(vote));
    }

    private NextTurn setNextTurnIfExistsNextTurn(String gameId, String userId) {
        NextTurn nextTurn = playerTurnPolicy
                .updateTurnWhenPlayerTurnIsValidated(gameTurnRepository.findGameTurnByGameId(gameId), userId)
                .setIfExistsNextTurn();
        return nextTurn;
    }


    private GameResultBaseDto createGameResultBaseDto(Game game, Vote vote) {
        return GameResultBaseDto.of(
                game.getId(),
                resultPolicy.checkWhoWin(game, vote.getMostVotedResult()) ? CITIZEN : LIAR,
                resultPolicy.getPlayersResultInfo(game, vote.getVotedResult(game.getLiarId()))
        );
    }

    /**
     * gameResult가 데이터 서버로 보낼 때, 1회만 보내지도록 message 설정
     * @param game
     */
    private void updateSendMessage(Game game) {
        gameRepository.save(game.sendMessage());
    }

    /**
     * 게임의 마지막 턴인 경우, vote 초기화 값을 저장한다.
     * @param gameId gameId
     * @param nextTurn nextTurn
     * @throws InterruptedException
     */
    private void saveVoteWhenLastTurnEnd(String gameId, NextTurn nextTurn) throws InterruptedException {
        if (nextTurn.getUserIdOfNextTurn() == null) {
            Game game = findGameByGameId(gameId);
            votePolicy.saveVote(game);
        }
    }

    /**
     * gameId로 Game을 찾는다.
     * @param gameId
     * @return game
     */
    private Game findGameByGameId(String gameId) {
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
        throw new NotFoundUserException();
    }

    private Vote getVote(Game game) {
        Vote vote = voteRepository.findVoteByGameId(game.getId());
        if (vote == null) throw new NotFoundVoteException();
        return vote;
    }
}
