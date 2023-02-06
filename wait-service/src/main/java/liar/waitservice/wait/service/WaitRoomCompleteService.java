package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.WaitRoomCompleteJoinMember;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteRepository;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteJoinMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitRoomCompleteService {

    private final WaitRoomCompleteRepository waitRoomCompleteRepository;
    private final WaitRoomCompleteJoinMemberRepository waitRoomCompleteJoinMemberRepository;

    public void save(WaitRoom waitRoom) {
        WaitRoomComplete waitRoomComplete = WaitRoomComplete.of(waitRoom);
        waitRoomCompleteRepository.save(waitRoomComplete);
        waitRoom.getMembers().stream().forEach(m -> waitRoomCompleteJoinMemberRepository.save(WaitRoomCompleteJoinMember.of(waitRoomComplete, m)));
    }

    public void updateWaitRoomCompleteStatusEnd(String roomId) {
        findWaitRoomCompleteByWaitRoomId(roomId).updateWaitRoomStatusDueToEndGame();
    }

    public WaitRoomComplete findWaitRoomCompleteByWaitRoomId(String roomId) {
        return waitRoomCompleteRepository.findWaitRoomCompleteByWaitRoomId(roomId).orElseThrow(NotFoundWaitRoomException::new);
    }

}
