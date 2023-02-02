package liar.waitservice.wait.service.search;

import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.WaitRoomRedisRepository;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomIdSearchServiceTest {

    @Autowired
    RoomIdSearchService roomIdSearchService;

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;

    @Autowired
    JoinMemberRedisRepository joinMemberRedisRepository;

    WaitRoom waitRoom;

    @BeforeEach
    public void init() {
        waitRoom = WaitRoom.of(new CreateWaitRoomDto("koseId", "kose1", 7), "koseName");
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom));
    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
        joinMemberRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("RoomId로 WaitRoom 찾기")
    public void searchRoomId() throws Exception {
        //given
        String roomId = waitRoom.getId();

        // when
        List<WaitRoomViewsDto> waitRoomViewsDtos = roomIdSearchService.searchWaitRoomByCond(roomId);

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(1);
        assertThat(waitRoomViewsDtos.get(0).getHostId()).isEqualTo("koseId");
        assertThat(waitRoomViewsDtos.get(0).getHostName()).isEqualTo("koseName");
        assertThat(waitRoomViewsDtos.get(0).getJoinMembersCnt()).isEqualTo(1);
        assertThat(waitRoomViewsDtos.get(0).getLimitsMembers()).isEqualTo(7);
        assertThat(waitRoomViewsDtos.get(0).getRoomId()).isEqualTo(roomId);

    }

    @Test
    @DisplayName("RoomId로 WaitRoom 찾기 (JoinMembers)")
    public void searchRoomId_manyMembers() throws Exception {
        //given
        for (int i = 0; i < 4; i++) {
            waitRoom.joinMembers(String.valueOf(i));
            joinMemberRedisRepository.save(JoinMember.of(String.valueOf(i), waitRoom.getId()));
        }
        waitRoomRedisRepository.save(waitRoom);

        //when
        List<WaitRoomViewsDto> waitRoomViewsDtos = roomIdSearchService.searchWaitRoomByCond(waitRoom.getId());

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(1);
        assertThat(waitRoomViewsDtos.get(0).getHostId()).isEqualTo("koseId");
        assertThat(waitRoomViewsDtos.get(0).getHostName()).isEqualTo("koseName");
        assertThat(waitRoomViewsDtos.get(0).getJoinMembersCnt()).isEqualTo(5);
        assertThat(waitRoomViewsDtos.get(0).getLimitsMembers()).isEqualTo(7);
        assertThat(waitRoomViewsDtos.get(0).getRoomId()).isEqualTo(waitRoom.getId());

    }

}