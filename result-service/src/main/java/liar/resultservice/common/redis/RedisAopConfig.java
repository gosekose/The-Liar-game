package liar.resultservice.common.redis;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class RedisAopConfig {

    @Bean
    public RedisLockAspect redisLockAspect(RedissonClient redissonClient) {
        return new RedisLockAspect(redissonClient);
    }



}
