package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.other.MemberService;
import liar.waitservice.other.dao.MemberNameOnly;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.UpdateWaitRoomStatusDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.redis.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import liar.waitservice.wait.service.policy.WaitRoomJoinPolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WaitRoomService {
    private final JoinMemberRedisRepository joinMemberRedisRepository;
    private final WaitRoomRedisRepository waitRoomRedisRepository;
    private final MemberService memberService;
    private final WaitRoomJoinPolicyService waitRoomJoinPolicyService;

    /**
     * search start
     */
    public WaitRoom findWaitRoomId(String roomId) {
        return waitRoomRedisRepository.findById(roomId).orElseThrow(NotExistsRoomIdException::new);
    }

    public WaitRoom findWaitRoomByHostId(String hostId) {
        return waitRoomRedisRepository.findWaitRoomByHostId(hostId).orElseThrow(NotExistsRoomIdException::new);
    }

    public List<WaitRoom> findWaitRoomByHostName(String hostName) {
        return waitRoomRedisRepository.findAllByHostName(hostName);
    }

    public List<WaitRoom> findWaitRoomByRoomName(String roomName) {
        return waitRoomRedisRepository.findAllByRoomName(roomName);
    }

    public Slice<WaitRoom> findWaitRoomByRoomId(String roomName, Pageable pageable) {
        return waitRoomRedisRepository.findWaitRoomById(roomName, pageable);
    }

    public Slice<WaitRoom> findWaitRoomByHostName(String hostName, Pageable pageable) {
        return waitRoomRedisRepository.findWaitRoomByHostName(hostName, pageable);
    }

    public Slice<WaitRoom> findWaitRoomByRoomName(String roomName, Pageable pageable) {
        return waitRoomRedisRepository.findWaitRoomByRoomName(roomName, pageable);
    }


    /**
     * search end
     */


    /**
     * waitRoom을 저장
     * createWaitRoomDto로 waitRoom의 정보를 얻고, userId로 hostName 불러오기
     * waitRoom을 redis에 저장하고, joinMembers를 생성하여 저장한다.
     */
    public String saveWaitRoom(CreateWaitRoomDto createWaitRoomDto) {
        waitRoomJoinPolicyService.createWaitRoomPolicy(createWaitRoomDto.getUserId());
        MemberNameOnly username = memberService.findUsernameById(createWaitRoomDto.getUserId());
        WaitRoom waitRoom = saveWaitRoomAndStatusJoin(createWaitRoomDto, username);
        return waitRoom.getId();
    }

    /**
     * 호스트가 아닌 다른 유저 대기방 요청 승인
     */
    public boolean addMembers(RequestWaitRoomDto requestWaitRoomDto) {
        waitRoomJoinPolicyService.joinWaitRoomPolicy(requestWaitRoomDto.getUserId());
        WaitRoom waitRoom = findById(requestWaitRoomDto.getRoomId());

        if (isEnableJoinMembers(requestWaitRoomDto, waitRoom)) {
            return saveWaitRoomAndStatusJoin(requestWaitRoomDto, waitRoom);
        }
        return false;

    }

    /**
     * 호스트가 아닌 다른 유저 대기방 나가기
     */
    public boolean leaveMember(RequestWaitRoomDto requestWaitRoomDto) {
        WaitRoom waitRoom = findById(requestWaitRoomDto.getRoomId());
        if (isLeaveMember(requestWaitRoomDto, waitRoom)) {
            saveWaitRoomAndStatusLeave(requestWaitRoomDto, waitRoom);
            return true;
        }
        return false;
    }

    /**
     * 대기방 탈퇴 요청이 호스트라면, 대기방에 참여한 인원의 join key를 삭제하고, 방의 정보 전체 삭제
     */
    public boolean deleteWaitRoomByHost(RequestWaitRoomDto join) {
        WaitRoom waitRoom = findById(join.getRoomId());

        if (isHost(waitRoom, join.getUserId())) {
            saveWaitRoomAndStatusLeave(waitRoom);
            return true;
        };
        return false;
    }

    /**
     * 게임이 성공적으로 종료된 경우, Redis에 저장된 waitRoom 제거
     */
    public boolean deleteWaitRoomBySuccessGameEndMsg(UpdateWaitRoomStatusDto<String> message) {
        try{
            WaitRoom waitRoom = findById(message.getRoomId());
            saveWaitRoomAndStatusLeave(waitRoom);
            return true;
        } catch (Exception e) {
            return false;
        }
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
        return waitRoomRedisRepository.findById(roomId).orElseThrow(NotExistsRoomIdException::new);
    }

    /**
     * waitRoom에 인원 추가가 가능하지 파악
     */
    private static boolean isEnableJoinMembers(RequestWaitRoomDto requestWaitRoomDto, WaitRoom waitRoom) {
        return waitRoom.joinMembers(requestWaitRoomDto.getUserId());
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
     * 유저기 방에 참여하면, 방에 추가된 인원을 저장하고 조인 상태 정보를 저장한다.
     */
    private boolean saveWaitRoomAndStatusJoin(RequestWaitRoomDto requestWaitRoomDto, WaitRoom waitRoom) {
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(requestWaitRoomDto));
        return true;
    }

    /**
     * 유저기 방에 퇴장하면, 방에 제거된 인원을 저장하고 조인 상태 정보를 삭제한다.
     */
    private void saveWaitRoomAndStatusLeave(RequestWaitRoomDto requestWaitRoomDto, WaitRoom waitRoom) {
        joinMemberRedisRepository.delete(JoinMember.of(requestWaitRoomDto));
        waitRoomRedisRepository.save(waitRoom);
    }

    /**
     * 호스트가 방에 퇴장하면, 방에 저장된 모든 유저의 조인 상태 정보를 삭제하고 방을 제거한다.
     */
    private void saveWaitRoomAndStatusLeave(WaitRoom waitRoom) {
        waitRoom.getMembers().stream().forEach(j -> joinMemberRedisRepository.delete(new JoinMember(j, waitRoom.getId())));
        waitRoomRedisRepository.delete(waitRoom);
    }
}
