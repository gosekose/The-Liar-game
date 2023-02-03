package liar.waitservice.wait.repository;

import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisTemplateTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;

    WaitRoom waitRoom;

    @BeforeEach
    public void init() {
        waitRoom = WaitRoom.of(new CreateWaitRoomDto("koseId", "koseRoomName", 7), "koseUsername");
    }

    @Test
    @DisplayName("waitRoom의 정보를 <String, Object> value로 저장하기")
    public void saveOpsValue() throws Exception {
        redisTemplate.opsForValue().set(waitRoom.getId(), waitRoom);
    }

    @Test
    @DisplayName("waitRoom 정보를 <String, Object> Hash로 저장하기")
    public void saveOpsHash() throws Exception {
        redisTemplate.opsForHash().put(waitRoom.getId(), waitRoom.getId(), waitRoom);
    }

}