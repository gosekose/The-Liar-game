package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.other.MemberService;
import liar.waitservice.other.dao.MemberNameOnly;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.PostProcessEndGameDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.WaitRoomCompleteJoinMember;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteJoinMemberRepository;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteRepository;
import liar.waitservice.wait.repository.redis.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import liar.waitservice.wait.service.join.WaitRoomJoinPolicyService;
import liar.waitservice.wait.service.start.DoProcessStartAndEndGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WaitRoomFacadeServiceImpl implements WaitRoomFacadeService {
    private final JoinMemberRedisRepository joinMemberRedisRepository;
    private final WaitRoomRedisRepository waitRoomRedisRepository;
    private final MemberService memberService;
    private final WaitRoomJoinPolicyService waitRoomJoinPolicyService;
    private final WaitRoomCompleteRepository waitRoomCompleteRepository;
    private final WaitRoomCompleteJoinMemberRepository waitRoomCompleteJoinMemberRepository;
    private final DoProcessStartAndEndGameService doProcessStartAndEndGameService;

    @Override
    public void save(WaitRoom waitRoom) {
        WaitRoomComplete waitRoomComplete = WaitRoomComplete.of(waitRoom);
        waitRoomCompleteRepository.save(waitRoomComplete);
        waitRoom.getMembers().stream().forEach(m -> waitRoomCompleteJoinMemberRepository.save(WaitRoomCompleteJoinMember.of(waitRoomComplete, m)));
    }

    @Override
    public void updateWaitRoomCompleteStatusEnd(String roomId) {
        findWaitRoomCompleteByWaitRoomId(roomId).updateWaitRoomStatusDueToEndGame();
    }

    @Override
    public WaitRoomComplete findWaitRoomCompleteByWaitRoomId(String roomId) {
        return waitRoomCompleteRepository.findWaitRoomCompleteByWaitRoomId(roomId).orElseThrow(NotFoundWaitRoomException::new);
    }

    @Override
    public void doPreProcessBeforeGameStart(RequestWaitRoomDto saveRequest) {
        doProcessStartAndEndGameService.doPreProcessBeforeGameStart(saveRequest);
    }

    @Override
    public void doPostProcessAfterGameEnd(PostProcessEndGameDto<String> request) {
        doProcessStartAndEndGameService.doPostProcessAfterGameEnd(request);
    }


    /**
     * waitRoom을 저장
     * createWaitRoomDto로 waitRoom의 정보를 얻고, userId로 hostName 불러오기
     * waitRoom을 redis에 저장하고, joinMembers를 생성하여 저장한다.
     */
    @Override
    public String saveWaitRoomByHost(CreateWaitRoomDto createWaitRoomDto) {
        waitRoomJoinPolicyService.createWaitRoomPolicy(createWaitRoomDto.getUserId());
        MemberNameOnly username = memberService.findUsernameById(createWaitRoomDto.getUserId());
        WaitRoom waitRoom = saveWaitRoomAndStatusJoin(createWaitRoomDto, username);
        return waitRoom.getId();
    }

    /**
     * 호스트가 아닌 다른 유저 대기방 요청 승인
     * 게임이 진행 중이거나 현재 게임 중인 유저인 경우, 현재 게임에 참여할 수 없음.
     */
    @Override
    public boolean addMembers(RequestWaitRoomDto dto) {

        if (!validateNotPlaying(dto.getRoomId(), dto.getUserId())) return false;
        waitRoomJoinPolicyService.joinWaitRoomPolicy(dto.getUserId());
        WaitRoom waitRoom = findById(dto.getRoomId());

        if (isEnableJoinMembers(dto, waitRoom)) {
            return saveWaitRoomAndStatusJoin(dto, waitRoom);
        }
        return false;

    }

    /**
     * waitRoom에 인원 추가가 가능하지 파악
     */
    public boolean isEnableJoinMembers(RequestWaitRoomDto requestWaitRoomDto, WaitRoom waitRoom) {
        return waitRoom.joinMembers(requestWaitRoomDto.getUserId());
    }


    /**
     * 호스트가 아닌 다른 유저 대기방 나가기
     */
    @Override
    public boolean leaveMember(RequestWaitRoomDto requestWaitRoomDto) {
        WaitRoom waitRoom = findById(requestWaitRoomDto.getRoomId());
        if (isLeaveMember(requestWaitRoomDto, waitRoom)) {
            deleteWaitRoomAndJoinMembers(requestWaitRoomDto, waitRoom);
            return true;
        }
        return false;
    }

    /**
     * 대기방 탈퇴 요청이 호스트라면, 대기방에 참여한 인원의 join key를 삭제하고, 방의 정보 전체 삭제
     */
    @Override
    public boolean deleteWaitRoomByHost(RequestWaitRoomDto request) {
        WaitRoom waitRoom = findById(request.getRoomId());

        if (isHost(waitRoom, request.getUserId())) {
            deleteWaitRoomAndJoinMembers(waitRoom);
            return true;
        };
        return false;
    }

    /**
     * 유저기 방에 참여하면, 방에 추가된 인원을 저장하고 조인 상태 정보를 저장한다.
     */
    private boolean saveWaitRoomAndStatusJoin(RequestWaitRoomDto dto, WaitRoom waitRoom) {
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(dto));
        return true;
    }

    /**
     * userId로 waitRoom의 호스트인지 파악
     */
    private static boolean isHost(WaitRoom waitRoom, String userId) {
        return waitRoom.isHost(userId);
    }

    /**
     * roomId로 waitRoomRedisRepository에서 waitRoom 가져오기
     */
    private WaitRoom findById(String roomId) {
        return waitRoomRedisRepository.findById(roomId).orElseThrow(NotFoundWaitRoomException::new);
    }

    /**
     * waitRoom에서 joinStatusWaitRoomDto로 온 userId가 방에서 나갔는지 파악
     * 없거나 실패한 경우 false
     * 나가서 저장되었다면 true
     */
    private static boolean isLeaveMember(RequestWaitRoomDto requestWaitRoomDto, WaitRoom waitRoom) {
        return waitRoom.leaveMember(requestWaitRoomDto.getUserId());
    }

    /**
     * 방을 개설할 때, 호스트의 방 개설과, 조인 상태 정보를 저장한다.
     */
    @NotNull
    private WaitRoom saveWaitRoomAndStatusJoin(CreateWaitRoomDto createWaitRoomDto, MemberNameOnly username) {
        WaitRoom waitRoom = waitRoomRedisRepository.save(WaitRoom.of(createWaitRoomDto, username.getUsername()));
        joinMemberRedisRepository.save(JoinMember.of(waitRoom));
        return waitRoom;
    }

    /**
     * 유저기 방에 퇴장하면, 방에 제거된 인원을 저장하고 조인 상태 정보를 삭제한다.
     */
    private void deleteWaitRoomAndJoinMembers(RequestWaitRoomDto requestWaitRoomDto, WaitRoom waitRoom) {
        joinMemberRedisRepository.delete(JoinMember.of(requestWaitRoomDto));
        waitRoomRedisRepository.save(waitRoom);
    }

    /**
     * 호스트가 방에 퇴장하면, 방에 저장된 모든 유저의 조인 상태 정보를 삭제하고 방을 제거한다.
     */
    private void deleteWaitRoomAndJoinMembers(WaitRoom waitRoom) {
        waitRoom.getMembers().stream().forEach(j -> joinMemberRedisRepository.delete(new JoinMember(j, waitRoom.getId())));
        waitRoomRedisRepository.delete(waitRoom);
    }

    private boolean validateNotPlaying(String waitRoomId, String userId) {
        return waitRoomJoinPolicyService.isNotPlayingUser(userId) &&
                waitRoomJoinPolicyService.isNotPlayingWaitRoom(waitRoomId) ;
    }
}
