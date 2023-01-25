package liar.memberservice.token.repository;

import liar.memberservice.token.domain.LogoutRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutRefreshTokenRedisRepository extends CrudRepository<LogoutRefreshToken, String> {
}
