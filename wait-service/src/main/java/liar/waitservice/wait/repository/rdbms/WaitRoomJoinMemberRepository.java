package liar.waitservice.wait.repository.rdbms;

import liar.waitservice.wait.domain.WaitRoomJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitRoomJoinMemberRepository extends JpaRepository<WaitRoomJoinMember, Long> {
}
