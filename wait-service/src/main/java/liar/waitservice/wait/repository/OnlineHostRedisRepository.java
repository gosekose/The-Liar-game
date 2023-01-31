package liar.waitservice.wait.repository;

import liar.waitservice.wait.domain.OnlineHost;
import org.springframework.data.repository.CrudRepository;

public interface OnlineHostRedisRepository extends CrudRepository<OnlineHost, String> {
    OnlineHost findOnlineHostById(String id);
}
