package liar.waitservice.wait.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import liar.waitservice.wait.domain.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
public class CrudValueCustomRepositoryImpl<T extends BaseEntity, R> implements CrudValueCustomRepository<T, R> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void set(T value) {
        String key = getKeyByValue(value);
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void delete(R id, Class<T> clazz) {
        String key = getKeyByClass(id, clazz);
        redisTemplate.delete(key);
    }

    @Override
    public T get(R id, Class<T> clazz) {
        String key = getKeyByClass(id, clazz);
        Object o = redisTemplate.opsForValue().get(key);

        if(o != null) {
            if(o instanceof LinkedHashMap){
                return (T) objectMapper.convertValue(o, clazz);
            }else{
                return (T) clazz.cast(o);
            }
        }
        return null;
    }

    @Override
    public boolean isExists(R id, Class<T> clazz) {
        String key = getKeyByClass(id, clazz);
        return redisTemplate.hasKey(key);
    }

    @NotNull
    private String getKeyByValue(T value) {
        return value.getClass().getSimpleName() + ":" + value.getId();
    }

    @NotNull
    private String getKeyByClass(R id, Class clazz) {
        return clazz.getSimpleName() + ":" + id;
    }
}
