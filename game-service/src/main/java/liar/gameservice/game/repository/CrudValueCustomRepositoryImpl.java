package liar.gameservice.game.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import liar.gameservice.exception.exception.EntityNotFoundException;
import liar.gameservice.game.domain.BaseRedisTemplateEntity;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
public class CrudValueCustomRepositoryImpl<T extends BaseRedisTemplateEntity, R> implements CrudValueCustomRepository<T, R> {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> set(T value) {
        String key = getKeyByValue(value);
        return redisTemplate.opsForValue().set(key, value).then();
    }

    @Override
    public Mono<Boolean> delete(R id, Class<T> clazz) {
        String key = getKeyByClass(id, clazz);
        return redisTemplate.delete(key)
                .map(deleteCount -> deleteCount > 0);
    }

    @Override
    public Mono<T> get(R id, Class<T> clazz) {
        String key = getKeyByClass(id, clazz);
        return redisTemplate.opsForValue().get(key)
                .map(o -> {
                    if (o instanceof LinkedHashMap<?,?>) {
                        return objectMapper.convertValue(o, clazz);
                    } else {
                        return clazz.cast(o);
                    }
                })
                .defaultIfEmpty(null);
    }

    @Override
    public Mono<Boolean> isExists(R id, Class<T> clazz) {
        String key = getKeyByClass(id, clazz);
        return redisTemplate.hasKey(key)
                .onErrorReturn(false);
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
