package liar.waitservice.wait.service.search;

import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.redis.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HostNameSearchServiceTest {

    @Autowired
    HostNameSearchService hostNameSearchService;

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
    @DisplayName("HostName으로 Room 찾기")
    public void searchWaitRoomByRoomName() throws Exception {
        //given-when
        String hostName = "koseUsername";

        //when
        List<WaitRoomViewsDto> waitRoomViewsDtos = hostNameSearchService.searchWaitRoomByCond(hostName);

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(1);
        assertThat(waitRoomViewsDtos.get(0).getHostId()).isEqualTo(waitRoom.getHostId());
        assertThat(waitRoomViewsDtos.get(0).getHostName()).isEqualTo(waitRoom.getHostName());
        assertThat(waitRoomViewsDtos.get(0).getJoinMembersCnt()).isEqualTo(1);
        assertThat(waitRoomViewsDtos.get(0).getLimitsMembers()).isEqualTo(waitRoom.getLimitMembers());
        assertThat(waitRoomViewsDtos.get(0).getRoomId()).isEqualTo(waitRoom.getId());

    }
    @Test
    @DisplayName("hostName으로 Room찾기 (방이 여러 개)")
    public void searchWaitRoomByRoomName_manyRoomName() throws Exception {
        //given
        for (int i = 0; i < 10; i++) {
            WaitRoom otherWaitRoom = WaitRoom.of(new CreateWaitRoomDto(String.valueOf(i), "koseRoomName", 7), "koseUsername");
            waitRoomRedisRepository.save(otherWaitRoom);
            joinMemberRedisRepository.save(JoinMember.of(otherWaitRoom));
        }

        //when
        List<WaitRoomViewsDto> waitRoomViewsDtos = hostNameSearchService.searchWaitRoomByCond("koseUsername");

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(11);
        assertThat(waitRoomViewsDtos.get(0).getHostName()).isEqualTo("koseUsername");
        assertThat(waitRoomViewsDtos.get(1).getHostName()).isEqualTo("koseUsername");
        assertThat(waitRoomViewsDtos.get(2).getHostName()).isEqualTo("koseUsername");
        assertThat(waitRoomViewsDtos.get(2).getRoomName()).isEqualTo("koseRoomName");
    }

}