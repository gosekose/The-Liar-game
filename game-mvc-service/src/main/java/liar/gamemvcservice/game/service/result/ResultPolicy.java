package liar.gamemvcservice.game.service.result;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.VotedResult;
import liar.gamemvcservice.game.service.dto.GameResultSaveMessage;
import liar.gamemvcservice.game.service.dto.GameResultToClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ResultPolicy {

    /**
     * 투표 결과 Liar 올바르게 선택함
     * 라이어 승리 or 시민 승리
     */
    boolean checkWhoWin(Game game, List<VotedResult> votedResults);

    /**
     * 게임 결과를 클라이언트에게 알리다.
     */
    GameResultToClient informGameResult(Game game, List<VotedResult> votedResults);

    /**
     * 게임 결과를 result 서버로 메세지 보낸다.
     */
    GameResultSaveMessage messageGameResult(Game game, GameResultToClient gameResult);

}
