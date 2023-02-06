package liar.waitservice.wait.repository.rdbms;

import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.WaitRoomCompleteJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitRoomCompleteJoinMemberRepository extends JpaRepository<WaitRoomCompleteJoinMember, Long> {

    List<WaitRoomCompleteJoinMember> findWaitRoomJoinMemberByWaitRoomComplete(WaitRoomComplete waitRoomComplete);
}
