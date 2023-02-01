package liar.waitservice.wait.repository;

import liar.waitservice.wait.domain.BaseEntity;

public interface CrudCustomRepository<T extends BaseEntity, R> {
    void set(T value);
    void delete(R id, Class<T> clazz);
    T get(R id, Class<T> clazz);
    boolean isExists(R key, Class<T> clazz);


}
