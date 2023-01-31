package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.JoinStatusWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.WaitRoomRedisRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WaitRoomServiceTest {

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;
    @Autowired
    WaitRoomService waitRoomService;

    CreateWaitRoomDto waitRoomDto;
    String hostId = "159b49cd-78d2-4b2d-8aa2-5b986b623251";

    @BeforeEach
    public void init() {
        waitRoomDto = new CreateWaitRoomDto(hostId, "game", 7);
    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("memberService에서 userName을 가져온 후 waitRoom을 생성하여 redis에 저장")
    public void saveWaitRoom() throws Exception {
        //given
        String roomId = waitRoomService.saveWaitRoom(waitRoomDto);

        //when
        WaitRoom waitRoom = waitRoomService.findRoomId(roomId);

        //then
        assertThat(roomId).isEqualTo(waitRoom.getId());
    }

    @Test
    @DisplayName("대기방에 입장 요청이 오면, 만석이 될 때 까지 waitRoom의 members에 저장한다.")
    public void addMembersSuccess() throws Exception {
        //given
        boolean[] results = new boolean[6];
        String roomId = waitRoomService.saveWaitRoom(waitRoomDto);

        //when
        for (int userId = 0; userId < 6; userId++) {
            results[userId] = waitRoomService.addMembers(new JoinStatusWaitRoomDto(String.valueOf(userId), roomId));
        }
        WaitRoom findRoom = waitRoomService.findRoomId(roomId);

        //then
        for (boolean joinStatus : results) {
            assertThat(joinStatus).isTrue();
        }
        assertThat(findRoom.getMembers().size()).isEqualTo(7);
        assertThat(findRoom.getMembers().get(3)).isEqualTo("2");
    }

    @Test
    @DisplayName("대기방에 입장 요청을 했을 때, 만석인 경우 저장하지 않고 false를 리턴한다.")
    public void addMembersFalseBecauseFullMembers() throws Exception {
        //given
        boolean[] results = new boolean[6];
        String roomId = waitRoomService.saveWaitRoom(waitRoomDto);

        //when
        for (int i = 0; i < 6; i++) {
            results[i] = waitRoomService.addMembers(new JoinStatusWaitRoomDto(String.valueOf(i), roomId));
        }
        boolean isSavedMembers = waitRoomService.addMembers(new JoinStatusWaitRoomDto("7", roomId));
        WaitRoom findRoom = waitRoomService.findRoomId(roomId);

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
        String roomId = waitRoomService.saveWaitRoom(waitRoomDto);
        for (int i = 0; i < 6; i++) {
            waitRoomService.addMembers(new JoinStatusWaitRoomDto(String.valueOf(i), roomId));
        }

        //when
        for (int i = 0; i < 3; i++) {
            results[i] = waitRoomService.leaveMember(new JoinStatusWaitRoomDto(String.valueOf(i), roomId));
        }
        WaitRoom findRoom = waitRoomService.findRoomId(roomId);

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
        String roomId = waitRoomService.saveWaitRoom(waitRoomDto);
        for (int i = 3; i < 9; i++) {
            waitRoomService.addMembers(new JoinStatusWaitRoomDto(String.valueOf(i), roomId));
        }

        //when
        for (int i = 0; i < 3; i++) {
            results[i] = waitRoomService.leaveMember(new JoinStatusWaitRoomDto(String.valueOf(i), roomId));
        }
        results[3] = waitRoomService.leaveMember(new JoinStatusWaitRoomDto(hostId, roomId));
        WaitRoom findRoom = waitRoomService.findRoomId(roomId);

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
        String roomId = waitRoomService.saveWaitRoom(waitRoomDto);

        //when
        boolean result = waitRoomService.deleteWaitRoomByHost(new JoinStatusWaitRoomDto(hostId, roomId));

        //then
        assertThat(result).isTrue();
        assertThatThrownBy(() -> waitRoomService.findRoomId(roomId))
                .isInstanceOf(NotExistsRoomIdException.class);
    }

    @Test
    @DisplayName("대기방의 호스트가 아닌 요청은, 방이 제거되지 않는다")
    public void deleteWaitRoomFalseBecauseNotHostId() throws Exception {
        //given
        String roomId = waitRoomService.saveWaitRoom(waitRoomDto);

        //when
        boolean result = waitRoomService.deleteWaitRoomByHost(new JoinStatusWaitRoomDto("1", roomId));

        //then
        assertThat(result).isFalse();
        assertThat(waitRoomService.findRoomId(roomId)).isNotNull();
    }
}