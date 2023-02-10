package liar.gameservice.game.service.policy.impl;

import liar.gameservice.game.domain.Topic;
import liar.gameservice.game.service.policy.interfaces.SetTopicPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SetTopicPolicyImplTest {

    @Autowired
    ReactiveRedisTemplate<String, Topic> reactiveRedisTemplate;

    @Autowired
    SetTopicPolicy setTopicPolicy;

//    @BeforeEach
//    public void setUp() {
//        for (long i = 0; i < 100; i++) {
//            Topic topic = new Topic(i, "Topic" + i);
//            reactiveRedisTemplate.opsForValue().set("Topic:" + topic.getId(), topic).subscribe();
//        }
//    }


    @Test
    @DisplayName("setTopic을 실행하면, range 범위 안에 있는 id를 찾아서, 랜덤한 topic을 가져온다.")
    public void setTopic_step() throws Exception {

        long range = (long) (Math.random() * 100) + 1;
        System.out.println("range = " + range);

        //given
        Mono<Topic> filter = Mono.just(range)
                .flatMap(topicId -> reactiveRedisTemplate.opsForValue().get("Topic:" + topicId))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.just(new Topic(1000L, "soccer")));
//
//        //when
        filter.subscribe(topic -> System.out.println("topic = " + topic.getId()));
        filter.subscribe(topic -> System.out.println("topic = " + topic.getName()));

        //then

    }

    @Test
    @DisplayName("setTopic을 실행하면, range 범위 안에 있는 id를 찾아서, 랜덤한 topic을 가져온다.")
    public void setTopic_Success() throws Exception {
        //given
        Mono<Topic> topicMono = setTopicPolicy.setTopic();

        //when
        topicMono.subscribe(topic -> System.out.println("topic.getId() = " + topic.getId()));
        topicMono.subscribe(topic -> System.out.println("topic.getName() = " + topic.getName()));

        //then
        StepVerifier.create(topicMono)
                .expectNextMatches(t -> t.getClass().isInstance(Topic.class))
                .expectComplete();

    }

    @Test
    @DisplayName("setTopic을 실행하면, range 범위 안에 있는 id를 찾아서, 랜덤한 topic을 가져온다.")
    public void setTopic_random() throws Exception {

        long range = (long) (Math.random() * 1000) + 10000;
        System.out.println("range = " + range);

        //given
        Mono<Topic> filter = Mono.just(range)
                .flatMap(topicId -> reactiveRedisTemplate.opsForValue().get("Topic:" + topicId))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.just(new Topic(1000L, "soccer")));
//
//        //when
        filter.subscribe(topic -> System.out.println("topic = " + topic.getId()));
        filter.subscribe(topic -> System.out.println("topic = " + topic.getName()));

        //then

    }

}