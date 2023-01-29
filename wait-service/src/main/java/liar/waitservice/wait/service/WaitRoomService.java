package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.JoinStatusWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.WaitRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitRoomService {

    private final WaitRoomRepository waitRoomRepository;

    public void saveWaitRoom(CreateWaitRoomDto createWaitRoomDto) {
        waitRoomRepository.save(WaitRoom.of(createWaitRoomDto));
    }

    /**
     * 호스트가 아닌 다른 유저 대기방 요청 승인
     */
    public boolean addMembers(JoinStatusWaitRoomDto joinStatusWaitRoomDto) {
        WaitRoom waitRoom = findByRoomId(joinStatusWaitRoomDto.getRoomId());
        return waitRoom.addMembers(joinStatusWaitRoomDto.getUserId());
    }

    /**
     * 호스트가 아닌 다른 유저 대기방 나가기
     */
    public void leaveMembers(JoinStatusWaitRoomDto joinStatusWaitRoomDto) {
        WaitRoom waitRoom = findByRoomId(joinStatusWaitRoomDto.getRoomId());
        waitRoom.leaveMembers(joinStatusWaitRoomDto.getUserId());
    }

    /**
     * 대기방 탈퇴 요청이 호스트라면, 대기방 전체 삭제
     */
    public void leaveWaitRoomByHost(JoinStatusWaitRoomDto join) {
        WaitRoom waitRoom = findByRoomId(join.getRoomId());

        if (isHost(waitRoom, join.getUserId())) {
            waitRoomRepository.delete(waitRoom);
        };
    }

    private static boolean isHost(WaitRoom waitRoom, String userId) {
        return waitRoom.isHost(userId);
    }

    private WaitRoom findByRoomId(String roomId) {
        return  waitRoomRepository.findWaitRoomByRoomId(roomId).orElseThrow(() -> {throw new NotExistsRoomIdException();});
    }

}
