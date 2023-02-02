package liar.waitservice.wait.repository;

import liar.waitservice.wait.domain.JoinMember;
import org.springframework.data.repository.CrudRepository;

public interface JoinMemberRedisRepository extends CrudRepository<JoinMember, String> {
    JoinMember findJoinMembersById(String userId);
}
