package liar.waitservice.wait.service.waitroom;

import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.WaitRoomCompleteJoinMember;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteRepository;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteJoinMemberRepository;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitRoomServiceImpl implements WaitRoomService {

    private final WaitRoomRedisRepository waitRoomRedisRepository;
    private final WaitRoomCompleteRepository waitRoomCompleteRepository;
    private final WaitRoomCompleteJoinMemberRepository waitRoomCompleteJoinMemberRepository;

    @Override
    public void saveWaitRoomComplete(WaitRoom waitRoom) {
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


    /**
     * search start
     */
    @Override
    public WaitRoom findWaitRoomId(String roomId) {
        return waitRoomRedisRepository.findById(roomId).orElseThrow(NotFoundWaitRoomException::new);
    }

    @Override
    public WaitRoom findWaitRoomByHostId(String hostId) {
        return waitRoomRedisRepository.findWaitRoomByHostId(hostId).orElseThrow(NotFoundWaitRoomException::new);
    }

    @Override
    public List<WaitRoom> findWaitRoomByHostName(String hostName) {
        return waitRoomRedisRepository.findAllByHostName(hostName);
    }

    @Override
    public List<WaitRoom> findWaitRoomByRoomName(String roomName) {
        return waitRoomRedisRepository.findAllByRoomName(roomName);
    }

    @Override
    public Slice<WaitRoom> findWaitRoomByRoomId(String roomName, Pageable pageable) {
        return waitRoomRedisRepository.findWaitRoomById(roomName, pageable);
    }

    @Override
    public Slice<WaitRoom> findWaitRoomByHostName(String hostName, Pageable pageable) {
        return waitRoomRedisRepository.findWaitRoomByHostName(hostName, pageable);
    }

    @Override
    public Slice<WaitRoom> findWaitRoomByRoomName(String roomName, Pageable pageable) {
        return waitRoomRedisRepository.findWaitRoomByRoomName(roomName, pageable);
    }
    /**
     * search end
     */

}
