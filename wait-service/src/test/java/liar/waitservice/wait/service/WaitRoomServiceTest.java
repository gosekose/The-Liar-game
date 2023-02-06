package liar.waitservice.wait.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.wait.MemberDummyInfo;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WaitRoomServiceTest extends MemberDummyInfo {

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;
    @Autowired
    WaitRoomService waitRoomService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    CreateWaitRoomDto waitRoomDto;

    @BeforeEach
    public void init() {
        waitRoomDto = new CreateWaitRoomDto(hostId, "game", 7);
    }

//    @AfterEach
//    public void tearDown() {
//        waitRoomRedisRepository.deleteAll();
//    }

    @Test
    @DisplayName("test")
    public void test() throws Exception {
        //given

        for (int i = 0; i < 15; i++) {
            waitRoomRedisRepository.save(
                    WaitRoom.of(new CreateWaitRoomDto(
                            "user" + i, "koseRoomName", 7), "koseUsername"));
        }
        //when

        //then

    }


    @Test
    @DisplayName("memberService에서 userName을 가져온 후 waitRoom을 생성하여 redis에 저장")
    public void saveWaitRoom() throws Exception {
        //given
        String roomId = waitRoomService.saveWaitRoomByHost(waitRoomDto);

        //when
        WaitRoom waitRoom = waitRoomService.findWaitRoomId(roomId);

        //then
        assertThat(roomId).isEqualTo(waitRoom.getId());
    }

    @Test
    @DisplayName("대기방에 입장 요청이 오면, 만석이 될 때 까지 waitRoom의 members에 저장한다.")
    public void addMembersSuccess() throws Exception {
        //given
        int limitMembers = 7;
        boolean[] results = new boolean[limitMembers - 1];

        CreateWaitRoomDto createWaitRoomDto = new CreateWaitRoomDto(hostId, "game", limitMembers);
        String roomId = waitRoomService.saveWaitRoomByHost(createWaitRoomDto);

        //when
        for (int i = 0; i < limitMembers - 1; i++) {
            results[i] = waitRoomService.addMembers(new RequestWaitRoomDto(String.valueOf(i), roomId));
        }
        WaitRoom findOne = waitRoomService.findWaitRoomId(roomId);

        //then
        assertThat(findOne.getMembers().size()).isEqualTo(7);
        assertThat(findOne.getMembers().get(1)).isEqualTo("0");
        for (boolean result : results) {
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("대기방에 입장 요청을 했을 때, 만석인 경우 저장하지 않고 false를 리턴한다.")
    public void addMembersFalseBecauseFullMembers() throws Exception {
        //given
        boolean[] results = new boolean[6];
        String roomId = waitRoomService.saveWaitRoomByHost(waitRoomDto);

        //when
        for (int i = 0; i < 6; i++) {
            results[i] = waitRoomService.addMembers(new RequestWaitRoomDto(String.valueOf(i), roomId));
        }
        boolean isSavedMembers = waitRoomService.addMembers(new RequestWaitRoomDto("7", roomId));
        WaitRoom findRoom = waitRoomService.findWaitRoomId(roomId);

        //then
        for (boolean joinStatus : results) {
            assertThat(joinStatus).isTrue();
        }
        assertThat(isSavedMembers).isFalse();
        assertThat(findRoom.getMembers().size()).isEqualTo(7);
        assertThat(findRoom.getMembers().get(3)).isEqualTo("2");
    }

    @Test
    @DisplayName("대기방에서 퇴장하면 값을 제거한 후 저장해야 한다.")
    public void leaveMemberTrue() throws Exception {
        //given
        boolean[] results = new boolean[6];
        String roomId = waitRoomService.saveWaitRoomByHost(waitRoomDto);
        for (int i = 0; i < 6; i++) {
            waitRoomService.addMembers(new RequestWaitRoomDto(String.valueOf(i), roomId));
        }

        //when
        for (int i = 0; i < 3; i++) {
            results[i] = waitRoomService.leaveMember(new RequestWaitRoomDto(String.valueOf(i), roomId));
        }
        WaitRoom findRoom = waitRoomService.findWaitRoomId(roomId);

        //then
        assertThat(results[0]).isTrue();
        assertThat(results[1]).isTrue();
        assertThat(results[2]).isTrue();
        assertThat(results[4]).isFalse();
        assertThat(findRoom.getMembers().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("대기방에 존재하는 호스트나 유저아이디가 아닌 경우 leaveMember는 false를 출력한다.")
    public void leaveMemberFalseBecauseNotCondition() throws Exception {
        //given
        boolean[] results = new boolean[4];
        String roomId = waitRoomService.saveWaitRoomByHost(waitRoomDto);
        for (int i = 3; i < 9; i++) {
            waitRoomService.addMembers(new RequestWaitRoomDto(String.valueOf(i), roomId));
        }

        //when
        for (int i = 0; i < 3; i++) {
            results[i] = waitRoomService.leaveMember(new RequestWaitRoomDto(String.valueOf(i), roomId));
        }
        results[3] = waitRoomService.leaveMember(new RequestWaitRoomDto(hostId, roomId));
        WaitRoom findRoom = waitRoomService.findWaitRoomId(roomId);

        //then
        for (boolean leaveStatus : results) {
            assertThat(leaveStatus).isFalse();
        }
        assertThat(findRoom.getMembers().size()).isEqualTo(7);
    }

    @Test
    @DisplayName("대기방 호스트가 퇴장하면, 방은 제거된다.")
    public void deleteWaitRoomTrue() throws Exception {
        //given
        String roomId = waitRoomService.saveWaitRoomByHost(waitRoomDto);

        //when
        boolean result = waitRoomService.deleteWaitRoomByHost(new RequestWaitRoomDto(hostId, roomId));

        //then
        assertThat(result).isTrue();
        assertThatThrownBy(() -> waitRoomService.findWaitRoomId(roomId))
                .isInstanceOf(NotFoundWaitRoomException.class);
    }

    @Test
    @DisplayName("대기방의 호스트가 아닌 요청은, 방이 제거되지 않는다")
    public void deleteWaitRoomFalseBecauseNotHostId() throws Exception {
        //given
        String roomId = waitRoomService.saveWaitRoomByHost(waitRoomDto);

        //when
        boolean result = waitRoomService.deleteWaitRoomByHost(new RequestWaitRoomDto("1", roomId));

        //then
        assertThat(result).isFalse();
        assertThat(waitRoomService.findWaitRoomId(roomId)).isNotNull();
    }
}