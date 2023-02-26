package liar.resultservice.result.service.save;

import liar.resultservice.other.member.Member;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.service.dto.SaveResultDto;
import org.springframework.stereotype.Service;

@Service
public interface SavePolicy {

    /**
     * gameResult를 저장하고 id를 반환
     * @return GameResult
     */
    GameResult saveGameResult(SaveResultDto saveResultDto);

    Player savePlayer(Member member);

    /**
     * player를 업데이트
     */
    void updatePlayer(GameResult gameResult, Player player, GameRole playerRole, Long exp);

    /**
     * playerResult를 저장하고 id를 반환
     * @return String
     */
    String savePlayerResult(String gameResultId, PlayerResultInfoDto dto, Long exp);

    Player getPlayer(PlayerResultInfoDto dto);

    Member getMember(PlayerResultInfoDto dto);

}
