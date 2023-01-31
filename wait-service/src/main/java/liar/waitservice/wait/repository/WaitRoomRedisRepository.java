package liar.waitservice.wait.repository;

import liar.waitservice.wait.domain.WaitRoom;
import org.springframework.data.repository.CrudRepository;

public interface WaitRoomRedisRepository extends CrudRepository<WaitRoom, String> {
}
