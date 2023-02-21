package liar.resultservice.result.service;

import liar.resultservice.result.service.dto.AllResultSavedWellDto;
import liar.resultservice.result.service.dto.RankTopDto;
import liar.resultservice.result.service.dto.SaveResultMessage;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public interface ResultFacadeService {

    /**
     * gameResult 관련 GameResult, player, playerResult 모두 저장
     * @return allResultSavedWellDto
     */
     AllResultSavedWellDto saveAllResultOfGame();

    /**
     * gameResult를 저장하고 id를 반환
     * @return boolean
     */
    boolean saveGameResult();

    /**
     * player를 저장하고 boolean를 반환
     * @return boolean
     */
    boolean savePlayer();

    /**
     * playerResult를 저장하고 id를 반환
     * @return boolean
     */
    boolean savePlayerResult();

    /**
     * 게임 결과가 저장되었다는 메세지를 전송
     * @return boolean
     */
    SaveResultMessage sendMessageThatResultIsSaved();

    /**
     *
     */
    Slice<RankTopDto> selectRankTop();
}
