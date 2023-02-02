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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomNameSearchServiceTest {

    @Autowired
    RoomNameSearchService roomNameSearchService;

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;

    @Autowired
    JoinMemberRedisRepository joinMemberRedisRepository;

    WaitRoom waitRoom;

    @BeforeEach
    public void init() {
        waitRoom = WaitRoom.of(new CreateWaitRoomDto("koseId", "koseRoomName", 7), "koseUsername");
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom));
    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
        joinMemberRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("RoomName으로 Room 찾기")
    public void searchWaitRoomByRoomName() throws Exception {
        //given-when
        String roomName = "koseRoomName";

        //when
        List<WaitRoomViewsDto> waitRoomViewsDtos = roomNameSearchService.searchWaitRoomByCond(roomName);

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(1);
        assertThat(waitRoomViewsDtos.get(0).getHostId()).isEqualTo(waitRoom.getHostId());
        assertThat(waitRoomViewsDtos.get(0).getHostName()).isEqualTo(waitRoom.getHostName());
        assertThat(waitRoomViewsDtos.get(0).getJoinMembersCnt()).isEqualTo(1);
        assertThat(waitRoomViewsDtos.get(0).getLimitsMembers()).isEqualTo(waitRoom.getLimitMembers());
        assertThat(waitRoomViewsDtos.get(0).getRoomId()).isEqualTo(waitRoom.getId());

    }
    @Test
    @DisplayName("RoomName으로 Room찾기 (방이 여러 개)")
    public void searchWaitRoomByRoomName_manyRoomName() throws Exception {
        //given
        for (int i = 0; i < 10; i++) {
            WaitRoom otherWaitRoom = WaitRoom.of(new CreateWaitRoomDto(String.valueOf(i), "koseRoomName", 7), "koseUsername");
            waitRoomRedisRepository.save(otherWaitRoom);
            joinMemberRedisRepository.save(JoinMember.of(otherWaitRoom));
        }

        //when
        List<WaitRoomViewsDto> waitRoomViewsDtos = roomNameSearchService.searchWaitRoomByCond("koseRoomName");

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(11);
        assertThat(waitRoomViewsDtos.get(0).getRoomName()).isEqualTo("koseRoomName");
        assertThat(waitRoomViewsDtos.get(1).getRoomName()).isEqualTo("koseRoomName");
        assertThat(waitRoomViewsDtos.get(2).getRoomName()).isEqualTo("koseRoomName");
        assertThat(waitRoomViewsDtos.get(2).getHostName()).isEqualTo("koseUsername");
    }

}