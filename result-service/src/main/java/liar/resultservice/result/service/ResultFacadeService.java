package liar.resultservice.result.service;

import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultDto;
import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import liar.resultservice.result.service.dto.SaveResultMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public interface ResultFacadeService {

    /**
     * gameResult 관련 GameResult, player, playerResult 모두 저장
     * @return allResultSavedWellDto
     */
    void saveAllResultOfGame(SaveResultRequest saveResultRequest);


    /**
     * gameResult를 저장하고 id를 반환
     * @return boolean
     */
    GameResult saveGameResult(SaveResultRequest saveResultRequest);

    /**
     * player를 저장하고 boolean를 반환
     * @return boolean
     */
    void savePlayer(GameResult gameResult, Player player, GameRole playerRole, Long exp);

    /**
     * playerResult를 저장하고 id를 반환
     * @return boolean
     */
    void savePlayerResult(SaveResultRequest request, GameResult gameResult,
                             PlayerResultInfoDto dto, Long exp);


    /**
     * 게임 결과가 저장되었다는 메세지를 전송
     * @return boolean
     */
    SaveResultMessage sendMessageThatResultIsSaved();

    /**
     * playerRanking을 slice 방식으로 가져온다.
     * @param pageable pageable
     * @return
     */
    Slice<PlayerRankingDto> fetchPlayerRank(Pageable pageable);

    /**
     * client의 개별 게임 결과를 가져온다.
     * @param cond cond
     * @param pageable pageable
     * @return
     */
    Slice<MyDetailGameResultDto> fetchMyDetailGameResult(MyDetailGameResultCond cond, Pageable pageable);
}
