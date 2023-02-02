package liar.waitservice.wait.service.search;

import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.SearchDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.SearchType;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.WaitRoomRedisRepository;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
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
class SearchConnectorTest {

    @Autowired
    SearchConnector searchConnector;

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
    @DisplayName("hostName 타입으로 검색하기")
    public void searchWaitRoomCondition_ByHostName() throws Exception {
        for (int i = 0; i < 10; i++) {
            WaitRoom otherWaitRoom = WaitRoom.of(new CreateWaitRoomDto(String.valueOf(i), String.valueOf(i), 7), "koseUsername");
            waitRoomRedisRepository.save(otherWaitRoom);
            joinMemberRedisRepository.save(JoinMember.of(otherWaitRoom));
        }

        //when
        List<WaitRoomViewsDto> waitRoomViewsDtos = searchConnector.searchWaitRoomCondition(new SearchDto("koseUsername", SearchType.HOSTNAME.getTypeName()));

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(11);
        assertThat(waitRoomViewsDtos.get(0).getHostName()).isEqualTo("koseUsername");
        assertThat(waitRoomViewsDtos.get(1).getHostName()).isEqualTo("koseUsername");
        assertThat(waitRoomViewsDtos.get(2).getHostName()).isEqualTo("koseUsername");
        assertThat(waitRoomViewsDtos.get(2).getHostName()).isEqualTo("koseUsername");
        assertThat(waitRoomViewsDtos.get(3).getRoomName()).isNotEqualTo(waitRoomViewsDtos.get(4).getRoomName());
    }

    @Test
    @DisplayName("RoomName 타입으로 검색하기")
    public void searchWaitRoomCondition_ByRoomName() throws Exception {
        for (int i = 0; i < 10; i++) {
            WaitRoom otherWaitRoom = WaitRoom.of(new CreateWaitRoomDto(String.valueOf(i), "koseRoomName", 7), String.valueOf(i));
            waitRoomRedisRepository.save(otherWaitRoom);
            joinMemberRedisRepository.save(JoinMember.of(otherWaitRoom));
        }

        //when
        List<WaitRoomViewsDto> waitRoomViewsDtos = searchConnector.searchWaitRoomCondition(new SearchDto("koseRoomName", SearchType.WAITROOMNAME.getTypeName()));

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(11);
        assertThat(waitRoomViewsDtos.get(0).getRoomName()).isEqualTo("koseRoomName");
        assertThat(waitRoomViewsDtos.get(1).getRoomName()).isEqualTo("koseRoomName");
        assertThat(waitRoomViewsDtos.get(2).getRoomName()).isEqualTo("koseRoomName");
        assertThat(waitRoomViewsDtos.get(2).getRoomName()).isEqualTo("koseRoomName");
        assertThat(waitRoomViewsDtos.get(4).getHostName()).isNotEqualTo(waitRoomViewsDtos.get(7).getHostName());
        assertThat(waitRoomViewsDtos.get(5).getHostName()).isNotEqualTo(waitRoomViewsDtos.get(8).getHostName());
        assertThat(waitRoomViewsDtos.get(6).getHostName()).isNotEqualTo(waitRoomViewsDtos.get(9).getHostName());
    }

    @Test
    @DisplayName("RoomName 타입으로 검색하기")
    public void searchWaitRoomCondition_ByRoomId() throws Exception {
        for (int i = 0; i < 10; i++) {
            WaitRoom otherWaitRoom = WaitRoom.of(new CreateWaitRoomDto(String.valueOf(i), "koseRoomName", 7), String.valueOf(i));
            waitRoomRedisRepository.save(otherWaitRoom);
            joinMemberRedisRepository.save(JoinMember.of(otherWaitRoom));
        }

        //when
        List<WaitRoomViewsDto> waitRoomViewsDtos = searchConnector.searchWaitRoomCondition(new SearchDto(waitRoom.getId(), SearchType.WAITROOMID.name()));

        //then
        assertThat(waitRoomViewsDtos.size()).isEqualTo(1);
        assertThat(waitRoomViewsDtos.get(0).getHostId()).isEqualTo(waitRoom.getHostId());
    }

}