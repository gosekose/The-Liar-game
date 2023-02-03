package liar.waitservice.wait.repository.redis;

import liar.waitservice.wait.domain.JoinMember;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JoinMemberRedisRepository extends CrudRepository<JoinMember, String> {
}
