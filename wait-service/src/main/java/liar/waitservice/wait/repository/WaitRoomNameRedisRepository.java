package liar.waitservice.wait.repository;

import liar.waitservice.wait.domain.WaitRoomName;
import org.springframework.data.repository.CrudRepository;

public interface WaitRoomNameRedisRepository extends CrudRepository<WaitRoomName, String> {
    WaitRoomName findWaitRoomNameById(String id);
}
