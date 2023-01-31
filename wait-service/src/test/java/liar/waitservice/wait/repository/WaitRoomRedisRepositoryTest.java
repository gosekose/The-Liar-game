package liar.waitservice.wait.repository;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WaitRoomRedisRepositoryTest {

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;
    private WaitRoom waitRoom;

    @BeforeEach
    void init() {
        CreateWaitRoomDto roomDto = new CreateWaitRoomDto("kose", "koseTest1", 5);
        waitRoom = WaitRoom.of(roomDto, "kose");
    }

    @AfterEach
    void tearDown() {
        waitRoomRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("Redis에 createWaitRoom 요청이 오면, 저장되어야 한다.")
    public void saveWaitRoom() throws Exception {
        //given
        waitRoomRedisRepository.save(waitRoom);

        //when
        WaitRoom findWaitRoom = findById(waitRoom.getId());

        //then
        assertThat(findWaitRoom.getRoomName()).isEqualTo("koseTest1");
        assertThat(findWaitRoom.getHostId()).isEqualTo("kose");
        assertThat(findWaitRoom.getLimitMembers()).isEqualTo(5);
        assertThat(findWaitRoom.getMembers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("대기방에 입장 요청이 오면, 인원을 추가하여 값을 변경하여 저장 해야 한다.")
    public void joinMembers() throws Exception {
        //given
        waitRoomRedisRepository.save(waitRoom);
        WaitRoom findRoom = findById(waitRoom.getId());

        //when
        findRoom.joinMembers("kose2");
        findRoom.joinMembers("kose3");
        findRoom.joinMembers("kose4");
        waitRoomRedisRepository.save(findRoom);
        WaitRoom result = findById(findRoom.getId());

        //then
        assertThat(result.getRoomName()).isEqualTo("koseTest1");
        assertThat(result.getHostId()).isEqualTo("kose");
        assertThat(result.getLimitMembers()).isEqualTo(5);
        assertThat(result.getMembers().size()).isEqualTo(4);
        assertThat(result.getMembers().get(1)).isEqualTo("kose2");
        assertThat(result.getMembers().stream().filter(f -> f.startsWith("kose")).collect(Collectors.toList()).size()).isEqualTo(4);
    }

    @Test
    @DisplayName("대기방에 있던 유저가 퇴장하면, 인원 변동되어 저장해야 한다.")
    public void leaveMembers() throws Exception {
        //given
        waitRoomRedisRepository.save(waitRoom);
        WaitRoom findRoom = findById(waitRoom.getId());
        findRoom.joinMembers("kose2");
        findRoom.joinMembers("kose3");
        findRoom.joinMembers("kose4");
        waitRoomRedisRepository.save(findRoom);

        //when
        WaitRoom room = findById(waitRoom.getId());
        room.leaveMembers("kose2");
        room.leaveMembers("kose4");
        WaitRoom result = waitRoomRedisRepository.save(room);

        //then
        assertThat(result.getRoomName()).isEqualTo("koseTest1");
        assertThat(result.getHostId()).isEqualTo("kose");
        assertThat(result.getLimitMembers()).isEqualTo(5);
        assertThat(result.getMembers().size()).isEqualTo(2);
        assertThat(result.getMembers().get(1)).isEqualTo("kose3");
    }

    @Test
    @DisplayName("대기방에 유저가 만석이 되면, 인원 입장이 불가하다")
    public void joinMembersDisable() throws Exception {
        //given
        waitRoomRedisRepository.save(waitRoom);
        WaitRoom findRoom = findById(waitRoom.getId());
        findRoom.joinMembers("kose2");
        findRoom.joinMembers("kose3");
        findRoom.joinMembers("kose4");
        findRoom.joinMembers("kose5");
        waitRoomRedisRepository.save(findRoom);

        //when
        boolean isJoinMember = findRoom.joinMembers("kose6");
        waitRoomRedisRepository.save(findRoom);
        WaitRoom result = findById(waitRoom.getId());

        //then
        assertThat(result.getRoomName()).isEqualTo("koseTest1");
        assertThat(result.getHostId()).isEqualTo("kose");
        assertThat(result.getLimitMembers()).isEqualTo(5);
        assertThat(result.getMembers().size()).isEqualTo(5);
        assertThat(result.getMembers().get(4)).isEqualTo("kose5");
        assertThat(!isJoinMember);
    }

    @Test
    @DisplayName("호스트가 대기방을 나가면, 방이 종료된다.")
    public void leaveHost() throws Exception {
        //given
        waitRoomRedisRepository.save(waitRoom);
        WaitRoom findRoom = findById(waitRoom.getId());
        findRoom.joinMembers("kose2");
        findRoom.joinMembers("kose3");
        findRoom.joinMembers("kose4");
        findRoom.joinMembers("kose5");
        WaitRoom result = waitRoomRedisRepository.save(findRoom);

        //when
        waitRoomRedisRepository.delete(result);

        //then
        Assertions.assertThatThrownBy(() -> findById(result.getId()))
                .isInstanceOf(NotExistsRoomIdException.class);
    }

    private WaitRoom findById(String id) {
        return waitRoomRedisRepository.findById(waitRoom.getId()).orElseThrow(NotExistsRoomIdException::new);
    }

}