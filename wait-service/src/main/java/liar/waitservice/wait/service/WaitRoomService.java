package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.other.MemberService;
import liar.waitservice.other.dao.MemberNameOnly;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.JoinStatusWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomName;
import liar.waitservice.wait.repository.WaitRoomRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class WaitRoomService {

    private final WaitRoomRedisRepository waitRoomRedisRepository;
    private final MemberService memberService;
    private final RedisTemplate redisTemplate;

    public WaitRoom findRoomId(String roomId) {
        return waitRoomRedisRepository.findById(roomId).orElseThrow(NotExistsRoomIdException::new);
    }


    public String saveWaitRoom(CreateWaitRoomDto createWaitRoomDto) {
        MemberNameOnly username = memberService.findUsernameById(createWaitRoomDto.getUserId());
        WaitRoom waitRoom = waitRoomRedisRepository.save(WaitRoom.of(createWaitRoomDto, username.getUsername()));
        return waitRoom.getId();
    }

    /**
     * 호스트가 아닌 다른 유저 대기방 요청 승인
     */
    public boolean addMembers(JoinStatusWaitRoomDto joinStatusWaitRoomDto) {

//        WaitRoom waitRoom = findById(joinStatusWaitRoomDto.getRoomId());
//        if (isEnableJoinMembers(joinStatusWaitRoomDto, waitRoom)) {
//            waitRoomRedisRepository.save(waitRoom);
//            return true;
//        }
//        return false;
//

        Object execute = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                WaitRoom waitRoom = (WaitRoom) operations.opsForValue().get(joinStatusWaitRoomDto.getRoomId());
                if (isEnableJoinMembers(joinStatusWaitRoomDto, waitRoom)) {
                    operations.opsForValue().set(waitRoom.getId(), waitRoom);
                }
                operations.exec();
                return true;
            }
        });

        return (boolean) execute;

    }

    /**
     * 호스트가 아닌 다른 유저 대기방 나가기
     */
    public boolean leaveMember(JoinStatusWaitRoomDto joinStatusWaitRoomDto) {
        WaitRoom waitRoom = findById(joinStatusWaitRoomDto.getRoomId());
        if (isLeaveMember(joinStatusWaitRoomDto, waitRoom)) {
            waitRoomRedisRepository.save(waitRoom);
            return true;
        }
        return false;
    }

    private static boolean isLeaveMember(JoinStatusWaitRoomDto joinStatusWaitRoomDto, WaitRoom waitRoom) {
        return waitRoom.leaveMember(joinStatusWaitRoomDto.getUserId());
    }

    /**
     * 대기방 탈퇴 요청이 호스트라면, 대기방 전체 삭제
     */
    public boolean deleteWaitRoomByHost(JoinStatusWaitRoomDto join) {
        WaitRoom waitRoom = findById(join.getRoomId());

        if (isHost(waitRoom, join.getUserId())) {
            waitRoomRedisRepository.delete(waitRoom);
            return true;
        };
        return false;
    }

    private static boolean isHost(WaitRoom waitRoom, String userId) {
        return waitRoom.isHost(userId);
    }

    private WaitRoom findById(String roomId) {
        return waitRoomRedisRepository.findById(roomId).orElseThrow(NotExistsRoomIdException::new);
    }

    private static boolean isEnableJoinMembers(JoinStatusWaitRoomDto joinStatusWaitRoomDto, WaitRoom waitRoom) {
        return waitRoom.joinMembers(joinStatusWaitRoomDto.getUserId());
    }
}
