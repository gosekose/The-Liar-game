package liar.waitservice.wait.service.waitroom;

import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WaitRoomService {

    void saveWaitRoomComplete(WaitRoom waitRoom);

    void updateWaitRoomCompleteStatusEnd(String roomId);

    WaitRoomComplete findWaitRoomCompleteByWaitRoomId(String roomId);

    /**
     * search start
     */
    WaitRoom findWaitRoomId(String roomId);
    WaitRoom findWaitRoomByHostId(String hostId);

    List<WaitRoom> findWaitRoomByHostName(String hostName);

    List<WaitRoom> findWaitRoomByRoomName(String roomName);

    Slice<WaitRoom> findWaitRoomByRoomId(String roomName, Pageable pageable);

    Slice<WaitRoom> findWaitRoomByHostName(String hostName, Pageable pageable);

    Slice<WaitRoom> findWaitRoomByRoomName(String roomName, Pageable pageable);
    /**
     * search end
     */


}
