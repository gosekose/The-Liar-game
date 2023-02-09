package liar.gameservice.game.repository;


import liar.gameservice.game.domain.BaseRedisTemplateEntity;

public interface CrudValueCustomRepository<T extends BaseRedisTemplateEntity, R> {
    void set(T value);
    void delete(R id, Class<T> clazz);
    T get(R id, Class<T> clazz);
    boolean isExists(R key, Class<T> clazz);


}
