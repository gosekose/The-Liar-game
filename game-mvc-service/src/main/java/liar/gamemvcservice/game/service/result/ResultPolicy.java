package liar.gamemvcservice.game.service.result;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.Vote;
import liar.gamemvcservice.game.domain.VotedResult;
import liar.gamemvcservice.game.service.dto.PlayerResultInfoDto;
import liar.gamemvcservice.game.service.dto.VotedResultDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ResultPolicy {

    /**
     * 투표 결과 Liar 올바르게 선택함
     * 라이어 승리 or 시민 승리
     */
    boolean checkWhoWin(Game game, List<VotedResult> votedResults);

    List<VotedResultDto> getVotedResultDto(Vote vote);
    List<PlayerResultInfoDto> getPlayersResultInfo(Game game, VotedResult votedResult);


}
