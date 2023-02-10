package liar.gameservice.game.repository;

import liar.gameservice.game.domain.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

@SpringBootTest
class CrudTest {

   @Autowired
   ReactiveRedisTemplate<String, Topic> redisTemplate;

    @Test
    @DisplayName("crud Test")
    public void setAndGetTest() throws Exception {
        //given
        Topic topic = new Topic(1L, "test1");
        //when
        Mono<Boolean> save = redisTemplate.opsForValue().set("topic:" + topic.getId(), topic);
        save.subscribe(System.out::println);

        Mono<Topic> topicMono = redisTemplate.opsForValue().get("topic:" + topic.getId());
        topicMono.subscribe(System.out::println);
        //then

    }

}