package liar.resultservice.result.service;

import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultDto;
import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import liar.resultservice.result.service.dto.SaveResultMessage;
import liar.resultservice.result.service.exp.ExpPolicy;
import liar.resultservice.result.service.myresult.MyGameResultPolicy;
import liar.resultservice.result.service.ranking.RankingPolicy;
import liar.resultservice.result.service.save.SavePolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
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
    private final RedissonClient redissonClient;
    private final SavePolicy savePolicy;
    /**
     * gameResult 관련 GameResult, player, playerResult 모두 저장
     *
     * @return allResultSavedWellDto
     */
    @Override
    public void saveAllResultOfGame(SaveResultRequest request) {
        GameResult gameResult = savePolicy.saveGameResult(request);
        request.getPlayersInfo()
                .stream()
                .forEach(playerDto -> {
                    Long exp = calculateExp(gameResult, playerDto);
                    Player player = null;
                    try {
                        player = savePolicy.getPlayer(playerDto);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    savePolicy.savePlayer(gameResult, player, playerDto.getGameRole(), exp);
                    savePolicy.savePlayerResult(request, gameResult.getId(), playerDto, exp);
                });
    }

    /**
     * 게임 결과가 저장되었다는 메세지를 전송
     *
     * @return boolean
     */
    @Override
    public SaveResultMessage sendMessageThatResultIsSaved() {
        return null;
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

    private Long calculateExp(GameResult gameResult, PlayerResultInfoDto playerDto) {
        return expPolicy
                .calculateExp(gameResult.getWinner(), playerDto.getGameRole() == gameResult.getWinner(),
                        playerDto.getAnswers(), gameResult.getTotalUsers());
    }
}
