package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.JoinStatusWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.WaitRoomRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitRoomService {

    private final WaitRoomRedisRepository waitRoomRedisRepository;

    public WaitRoom findRoomId(String roomId) {
        return waitRoomRedisRepository.findById(roomId).orElseThrow(NotExistsRoomIdException::new);
    }

    public void saveWaitRoom(CreateWaitRoomDto createWaitRoomDto, String userNAme) {
        waitRoomRedisRepository.save(WaitRoom.of(createWaitRoomDto, userNAme));
    }

    /**
     * 호스트가 아닌 다른 유저 대기방 요청 승인
     */
    public boolean addMembers(JoinStatusWaitRoomDto joinStatusWaitRoomDto) {
        WaitRoom waitRoom = findById(joinStatusWaitRoomDto.getRoomId());
        return waitRoom.joinMembers(joinStatusWaitRoomDto.getUserId());
    }

    /**
     * 호스트가 아닌 다른 유저 대기방 나가기
     */
    public void leaveMembers(JoinStatusWaitRoomDto joinStatusWaitRoomDto) {
        WaitRoom waitRoom = findById(joinStatusWaitRoomDto.getRoomId());
        waitRoom.leaveMembers(joinStatusWaitRoomDto.getUserId());
    }

    /**
     * 대기방 탈퇴 요청이 호스트라면, 대기방 전체 삭제
     */
    public void leaveWaitRoomByHost(JoinStatusWaitRoomDto join) {
        WaitRoom waitRoom = findById(join.getRoomId());

        if (isHost(waitRoom, join.getUserId())) {
            waitRoomRedisRepository.delete(waitRoom);
        };
    }

    private static boolean isHost(WaitRoom waitRoom, String userId) {
        return waitRoom.isHost(userId);
    }

    private WaitRoom findById(String roomId) {
        return  waitRoomRedisRepository.findById(roomId).orElseThrow(NotExistsRoomIdException::new);
    }
}
