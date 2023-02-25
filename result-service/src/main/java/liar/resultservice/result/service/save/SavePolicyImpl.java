package liar.resultservice.result.service.save;

import jakarta.persistence.LockModeType;
import liar.resultservice.exception.exception.NotFoundGameResultException;
import liar.resultservice.exception.exception.NotFoundTopicException;
import liar.resultservice.exception.exception.NotFoundUserException;
import liar.resultservice.other.member.Member;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.domain.PlayerResult;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import liar.resultservice.result.service.exp.ExpPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SavePolicyImpl implements SavePolicy {

    private final TopicRepository topicRepository;
    private final GameResultRepository gameResultRepository;
    private final ExpPolicy expPolicy;
    private final PlayerRepository playerRepository;
    private final PlayerResultRepository playerResultRepository;
    private final MemberRepository memberRepository;

    /**
     * gameResult를 저장하고 id를 반환
     * @return GameResult
     */
    @Override
    @Transactional
    public GameResult saveGameResult(SaveResultRequest request) {
        Topic topic = topicRepository.findById(request.getTopicId()).orElseThrow(NotFoundTopicException::new);
        GameResult gameResult = gameResultRepository.save(
                GameResult.builder()
                        .gameId(request.getGameId())
                        .gameName(request.getGameName())
                        .hostId(request.getHostId())
                        .roomId(request.getRoomId())
                        .topic(topic)
                        .winner(request.getWinner())
                        .totalUsers(request.getTotalUserCnt())
                        .build());
        return gameResult;
    }

    @Override
    @Transactional
    public Player savePlayer(Member member) {
        return playerRepository.save(Player.of(member));
    }

    /**
     * player를 업데이트
     */
    @Override
    @Transactional
    public void updatePlayer(GameResult gameResult, Player player, GameRole playerRole, Long exp) {
        player.levelUp(expPolicy.nextLevel(player.updateExp(exp)));
        player.updateGameResult(playerRole == gameResult.getWinner());
        playerRepository.saveAndFlush(player);
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
        log.info("gameResult = {}", gameResult.getId());
        return playerResultRepository.save(PlayerResult.builder()
                .gameResult(gameResult)
                .userId(dto.getUserId())
                .answers(dto.getAnswers())
                .isWin(dto.getGameRole() == gameResult.getWinner())
                .gameRole(dto.getGameRole())
                .exp(exp)
                .build()).getId();
    }



    @Override
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public Player getPlayer(PlayerResultInfoDto dto) {
        Member member = getMember(dto);
        Player player = playerRepository.findWithMemberForUpdate(member);
        if (player == null) {
            log.info("처음 요청: dto.userId = {}", dto.getUserId());
            return playerRepository.save(Player.of(member));
        }
        log.info("dto.userId = {}", dto.getUserId());
        log.info("이미 player가 있으므로 player 리턴 = {}", player.getMember().getId());
        return player;
    }

    @Transactional(readOnly = true)
    public Member getMember(PlayerResultInfoDto dto) {
        Member member = memberRepository.findByUserId(dto.getUserId());
        if (member == null) {
            throw new NotFoundUserException();
        }
        return member;
    }

}
