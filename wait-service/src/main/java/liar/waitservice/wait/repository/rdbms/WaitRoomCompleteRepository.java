package liar.waitservice.wait.repository.rdbms;

import liar.waitservice.wait.domain.WaitRoomComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitRoomCompleteRepository extends JpaRepository<WaitRoomComplete, Long> {

    List<WaitRoomComplete> findByHostId(String hostId);
    Optional<WaitRoomComplete> findWaitRoomCompleteByWaitRoomId(String waitRoomId);
}
