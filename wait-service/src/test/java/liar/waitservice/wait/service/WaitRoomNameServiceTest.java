package liar.waitservice.wait.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WaitRoomNameServiceTest {
    @Autowired
    WaitRoomNameService waitRoomNameService;

    @BeforeEach
    public void init() {

    }

    @AfterEach
    public void tearDown() {

    }

//    @Test
//    @DisplayName("webRoomName은 transaction 처리가 되어 현재 저장된 값이 없다면 저장되어야 한다.")
//    public void () throws Exception {
//        //given
//
//        //when
//
//        //then
//
//    }


}