package liar.resultservice.result.service;

import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultDto;
import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import liar.resultservice.result.service.dto.SaveInitPlayerDto;
import liar.resultservice.result.service.dto.SaveResultDto;
import liar.resultservice.result.service.dto.SaveResultMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ResultFacadeService {

    /**
     * gameResult 관련 GameResult, player, playerResult 모두 저장
     * @return allResultSavedWellDto
     */
    void saveAllResultOfGame(SaveResultDto saveResultDto);

    /**
     * player를 생성하여 저장한다.
     * @param saveInitPlayerDto
     * @return playerId
     */
    String savePlayer(SaveInitPlayerDto saveInitPlayerDto);

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
