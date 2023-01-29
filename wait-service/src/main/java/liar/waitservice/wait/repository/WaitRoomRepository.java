package liar.waitservice.wait.repository;

import liar.waitservice.wait.domain.WaitRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WaitRoomRepository extends CrudRepository<WaitRoom, String> {

    Optional<WaitRoom> findWaitRoomByRoomId(String roomId);

}
