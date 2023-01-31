package liar.waitservice.wait.service;

import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.repository.WaitRoomRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WaitRoomServiceTest {

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;
    @Autowired
    WaitRoomService waitRoomService;
    CreateWaitRoomDto waitRoomDto;

    @BeforeEach
    public void init() {
        waitRoomDto = new CreateWaitRoomDto("kose1", "game", 7);
    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
    }

//    @Test
//    @DisplayName("waitRoom을 생성하여 redis에 저장")
//    public void saveWaitRoom() throws Exception {
//        //given
//        waitRoomService.saveWaitRoom(waitRoomDto);
//
//        //when
//
//
//        //then
//
//    }



}