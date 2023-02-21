package liar.gamemvcservice.game.service;

import liar.gamemvcservice.game.controller.dto.request.VoteLiarRequest;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.service.dto.CommonDto;
import liar.gamemvcservice.game.service.dto.GameResultToServerDto;
import liar.gamemvcservice.game.service.dto.GameResultToClientDto;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GameFacadeService {

    /**
     * 방장의 요청을 받아 game을 저장한다,
     * @param dto game 설정 정보
     * @return gameId
     */
    String save(SetUpGameDto dto);


    /**
     * 플레이어의 역할을 조회한다.
     * @param dto gameId, userId
     * @return player 객체
     */
    Player checkPlayerRole(CommonDto dto);

    /**
     * 클라이언트의 role이 CITIZEN이면, topic을 반환한다.
     * @param dto gameId, userId
     * @return topic (게임 설명 주제)
     */
    Topic checkTopic(CommonDto dto);

    /**
     * gameId, userId로 joinPlayer 값을 조회한다.
     * @return joinPlayer
     */
    JoinPlayer findJoinPlayer(String gameId, String userId);

    /**
     * gameId를 받아, 게임의 턴을 설정한다.
     * @param gameId gameId
     * @return gameTurns(userId로 구성)
     */
    List<String> setUpTurn(String gameId);

    /**
     * 플레이어의 턴을 업데이트하고, 마지막 턴이라면 턴의 결과를 알리며, vote를 초기화하여 저장한다.
     * @return nextTurn (다음턴 userId, 마지막 턴 boolean)
     * @throws InterruptedException
     */
    NextTurn setNextTurnWhenValidated(String gameId, String userId) throws InterruptedException;


    /**
     * 클라이언트의 개별 투표를 저장한다.
     * gameId(게임 Id), userId(클라이언트의 userId), liarId(라이어로 지목할 클라이언트 userId)
     * @return vote가 수정되어 저장되면 true, 아니라면 false
     * @throws InterruptedException
     */
    boolean voteLiarUser(VoteLiarRequest dto) throws InterruptedException;

    /**
     * gameResult를 client에게 전달하는 Dto를 생성한다.
     * @param gameId
     * @return gameResultToClientDto
     */
    GameResultToClientDto sendGameResultToClient(String gameId);

    /**
     * 같은 게임을 공유하는 gameResultToServer가 처음 실행 되면 game save message를 보낸다.
     * @param gameId
     * @return gameResultToserverDto
     */
    GameResultToServerDto sendGameResultToServer(String gameId);

}
