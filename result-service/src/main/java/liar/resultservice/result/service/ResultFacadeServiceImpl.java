package liar.resultservice.result.service;

import liar.resultservice.exception.exception.NotFoundUserException;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultDto;
import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import liar.resultservice.result.service.dto.SaveInitPlayerDto;
import liar.resultservice.result.service.dto.SaveResultDto;
import liar.resultservice.result.service.dto.SaveResultMessage;
import liar.resultservice.result.service.exp.ExpPolicy;
import liar.resultservice.result.service.myresult.MyGameResultPolicy;
import liar.resultservice.result.service.ranking.RankingPolicy;
import liar.resultservice.result.service.save.SavePolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResultFacadeServiceImpl implements ResultFacadeService {
    private final ExpPolicy expPolicy;
    private final RankingPolicy rankingPolicy;
    private final MyGameResultPolicy myGameResultPolicy;
    private final SavePolicy savePolicy;
    /**
     * gameResult 관련 GameResult, player, playerResult 모두 저장
     *
     * @return allResultSavedWellDto
     */
    @Override
    public void saveAllResultOfGame(SaveResultDto dto) {
        GameResult gameResult = savePolicy.saveGameResult(dto);
        dto.getPlayersInfo()
                .stream()
                .forEach(playerDto -> {
                    Long exp = calculateExp(gameResult, playerDto);
                    savePolicy.updatePlayer(gameResult, savePolicy.getPlayer(playerDto), playerDto.getGameRole(), exp);
                    savePolicy.savePlayerResult(gameResult.getId(), playerDto, exp);
                });
    }

    /**
     * playerRanking을 slice 방식으로 가져온다.
     * @param pageable pageable
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Slice<PlayerRankingDto> fetchPlayerRank(Pageable pageable) {
        return rankingPolicy.fetchPlayerRanking(pageable);
    }

    /**
     * client의 개별 게임 결과를 가져온다.
     * @param cond     cond
     * @param pageable pageable
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Slice<MyDetailGameResultDto> fetchMyDetailGameResult(MyDetailGameResultCond cond, Pageable pageable) {
        return myGameResultPolicy.fetchMyGameResultInfoByCond(cond, pageable);
    }

    /**
     * exp 계산하여 exp를 리턴한다.
     * @param gameResult
     * @param playerDto
     * @return Long exp
     */
    private Long calculateExp(GameResult gameResult, PlayerResultInfoDto playerDto) {
        return expPolicy
                .calculateExp(gameResult.getWinner(), playerDto.getGameRole() == gameResult.getWinner(),
                        playerDto.getAnswers(), gameResult.getTotalUsers());
    }
}
