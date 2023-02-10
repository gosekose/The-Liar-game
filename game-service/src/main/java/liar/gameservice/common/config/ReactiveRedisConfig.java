package liar.gameservice.common.config;

import liar.gameservice.game.domain.Game;
import liar.gameservice.game.domain.JoinPlayer;
import liar.gameservice.game.domain.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

@Configuration
//@EnableTransactionManagement
@EnableRedisRepositories
public class ReactiveRedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public ReactiveRedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ReactiveRedisTemplate<String, JoinPlayer> joinPlayerReactiveRedisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<JoinPlayer> serializer = new Jackson2JsonRedisSerializer<>(JoinPlayer.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, JoinPlayer> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, JoinPlayer> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }


    @Bean
    public ReactiveRedisTemplate<String, Game> gameReactiveRedisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Game> serializer = new Jackson2JsonRedisSerializer<>(Game.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Game> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Game> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, Topic> topicReactiveRedisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Topic> serializer = new Jackson2JsonRedisSerializer<>(Topic.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Topic> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Topic> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}
