package liar.resultservice.result.service;

import jakarta.transaction.Transactional;
import liar.resultservice.exception.exception.NotFoundGameResultException;
import liar.resultservice.exception.exception.NotFoundUserException;
import liar.resultservice.other.member.Member;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.domain.PlayerResult;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultDto;
import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import liar.resultservice.result.service.dto.SaveResultMessage;
import liar.resultservice.result.service.exp.ExpPolicy;
import liar.resultservice.result.service.myresult.MyGameResultPolicy;
import liar.resultservice.result.service.ranking.RankingPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultFacadeServiceImpl implements ResultFacadeService {

    private final TopicRepository topicRepository;
    private final GameResultRepository gameResultRepository;
    private final PlayerResultRepository playerResultRepository;
    private final MemberRepository memberRepository;
    private final PlayerRepository playerRepository;
    private final ExpPolicy expPolicy;
    private final RankingPolicy rankingPolicy;
    private final MyGameResultPolicy myGameResultPolicy;

    /**
     * gameResult 관련 GameResult, player, playerResult 모두 저장
     *
     * @return allResultSavedWellDto
     */
    @Override
    @Transactional
    public void saveAllResultOfGame(SaveResultRequest request) {
        GameResult gameResult = saveGameResult(request);
        request.getPlayersInfo()
                .stream()
                .forEach(playerDto -> {
                            Long exp = calculateExp(gameResult, playerDto);
                            Player player = getPlayer(playerDto);
                            savePlayer(gameResult, player, playerDto.getGameRole(), exp);
                            savePlayerResult(request, gameResult.getId(), playerDto, exp);});
    }

    /**
     * gameResult를 저장하고 id를 반환
     *
     * @return boolean
     */
    @Override
    @Transactional
    public GameResult saveGameResult(SaveResultRequest request) {
        Topic topic = topicRepository.findById(request.getTopicId()).orElseThrow();
        return gameResultRepository.save(
                GameResult.builder()
                        .gameId(request.getGameId())
                        .gameName(request.getGameName())
                        .hostId(request.getHostId())
                        .roomId(request.getRoomId())
                        .topic(topic)
                        .winner(request.getWinner())
                        .totalUsers(request.getTotalUserCnt())
                        .build());
    }

    /**
     * player를 저장하고 boolean를 반환
     *
     * @return boolean
     */
    @Override
    @Transactional
    public void savePlayer(GameResult gameResult, Player player, GameRole playerRole, Long exp) {
        player.levelUp(expPolicy.nextLevel(player.updateExp(exp)));
        player.updateGameResult(playerRole == gameResult.getWinner());
    }

    /**
     * playerResult를 저장하고 id를 반환
     *
     * @return String
     */
    @Override
    @Transactional
    public String savePlayerResult(SaveResultRequest request, String gameResultId,
                                    PlayerResultInfoDto dto, Long exp) {

        GameResult gameResult = gameResultRepository.findById(gameResultId).orElseThrow(NotFoundGameResultException::new);
        return playerResultRepository.save(PlayerResult.builder()
                .gameResult(gameResult)
                .userId(dto.getUserId())
                .answers(dto.getAnswers())
                .isWin(dto.getGameRole() == gameResult.getWinner())
                .gameRole(dto.getGameRole())
                .exp(exp)
                .build()).getId();
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
    @Override
    public Slice<MyDetailGameResultDto> fetchMyDetailGameResult(MyDetailGameResultCond cond, Pageable pageable) {
        return myGameResultPolicy.fetchMyGameResultInfoByCond(cond, pageable);
    }

    private Long calculateExp(GameResult gameResult, PlayerResultInfoDto playerDto) {
        return expPolicy
                .calculateExp(gameResult.getWinner(), playerDto.getGameRole() == gameResult.getWinner(),
                        playerDto.getAnswers(), gameResult.getTotalUsers());
    }

    @Transactional
    private Player getPlayer(PlayerResultInfoDto dto) {
        Member member = getMember(dto);
        Player player = playerRepository.findPlayerByMember(member);
        if (player == null) {
            return playerRepository.save(Player.of(member));
        }
        return player;
    }

    @Transactional
    private Member getMember(PlayerResultInfoDto dto) {
        Member member = memberRepository.findByUserId(dto.getUserId());
        if (member == null) {
            throw new NotFoundUserException();
        }
        return member;
    }
}
