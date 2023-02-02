package liar.waitservice.wait.repository;

import liar.waitservice.wait.domain.WaitRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WaitRoomRedisRepository extends CrudRepository<WaitRoom, String> {
    List<WaitRoom> findAllByHostName(String hostName);
    List<WaitRoom> findAllByRoomName(String roomName);

    Optional<WaitRoom> findWaitRoomByHostId(String hostId);
    WaitRoom findByHostId(String hostId);
}
