package liar.memberservice.token.repository;


import liar.memberservice.token.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
