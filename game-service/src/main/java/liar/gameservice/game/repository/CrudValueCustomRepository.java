package liar.gameservice.game.repository;


import liar.gameservice.game.domain.BaseRedisTemplateEntity;
import reactor.core.publisher.Mono;

public interface CrudValueCustomRepository<T extends BaseRedisTemplateEntity, R> {
    Mono<Void> set(T value);
    Mono<Boolean> delete(R id, Class<T> clazz);
    Mono<T> get(R id, Class<T> clazz);
    Mono<Boolean> isExists(R key, Class<T> clazz);


}
