package liar.waitservice.wait.repository.redis.crud;

import liar.waitservice.wait.domain.utils.BaseRedisTemplateEntity;

public interface CrudValueCustomRepository<T extends BaseRedisTemplateEntity, R> {
    void set(T value);
    void delete(R id, Class<T> clazz);
    T get(R id, Class<T> clazz);
    boolean isExists(R key, Class<T> clazz);


}
