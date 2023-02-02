package liar.waitservice.wait.service.policy;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.JoinMemberRedisRepository;
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
class WaitRoomJoinPolicyServiceTest {

    @Autowired
    WaitRoomOnlyOneJoinPolicyService waitRoomOnlyOneJoinPolicyService;

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;

    @Autowired
    JoinMemberRedisRepository joinMemberRedisRepository;

    WaitRoom savedWaitRoom;
    String hostId = "159b49cd-78d2-4b2d-8aa2-5b986b623251";
    String hostName = "kose";

    @BeforeEach
    public void init() {
        savedWaitRoom = WaitRoom.of(new CreateWaitRoomDto(hostId, "game", 7), hostName);
        waitRoomRedisRepository.save(savedWaitRoom);
        joinMemberRedisRepository.save(JoinMember.of(savedWaitRoom));
    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
        joinMemberRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("host는 같은 시간에 하나의 방만 만들고 운영할 수 있다.")
    public void createWaitRoomPolicy_createOneWaitRoomByHost() throws Exception {
        //given
        WaitRoom waitRoom = WaitRoom.of(new CreateWaitRoomDto(hostId, "game2", 7), hostName);

        //when
        waitRoomOnlyOneJoinPolicyService.createWaitRoomPolicy(hostId);
        waitRoomRedisRepository.save(waitRoom);

        //then
        WaitRoom findWaitRoom = waitRoomRedisRepository.findByHostId(hostId);
        assertThat(findWaitRoom.getHostId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("user는 같은 시간에 하나의 방만 입장할 수 있다. (유저)")
    public void joinWaitRoomPolicy_joinOneWaitRoomToUser() throws Exception {
        //given
        String joinId = "join-user1";

        WaitRoom waitRoom2 = WaitRoom.of(new CreateWaitRoomDto("kose1", "game2", 7), "kose");

        waitRoomRedisRepository.save(waitRoom2);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom2));

        savedWaitRoom.joinMembers(joinId);
        waitRoomRedisRepository.save(savedWaitRoom);
        joinMemberRedisRepository.save(JoinMember.of(joinId, savedWaitRoom.getId()));

        //when
        waitRoomOnlyOneJoinPolicyService.joinWaitRoomPolicy(joinId);
        WaitRoom changedWaitRoom1 = waitRoomRedisRepository.findById(savedWaitRoom.getId()).orElseThrow(NotExistsRoomIdException::new);
        waitRoom2.joinMembers(joinId);
        WaitRoom changedWaitRoom2 = waitRoomRedisRepository.save(waitRoom2);
        joinMemberRedisRepository.save(JoinMember.of(joinId, waitRoom2.getId()));

        JoinMember joinMember = joinMemberRedisRepository.findById(joinId).orElseThrow(NotExistsRoomIdException::new);

        //then
        assertThat(changedWaitRoom1.getMembers().size()).isEqualTo(1);
        assertThat(joinMember.getId()).isEqualTo(joinId);
        assertThat(joinMember.getRoomId()).isEqualTo(changedWaitRoom2.getId());
    }

    @Test
    @DisplayName("호스트가 방을 만들고 다른 방에 입장하는 경우 개설된 호스트가 만든 방과 join 정보는 제거 해야한다.")
    public void joinWaitRoomPolicy_joinOneWaitRoomToHost() throws Exception {
        //given
        WaitRoom waitRoom2 = WaitRoom.of(new CreateWaitRoomDto("kose1", "game2", 7), "kose");
        waitRoomRedisRepository.save(waitRoom2);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom2));

        waitRoom2.joinMembers(hostId);
        waitRoomRedisRepository.save(waitRoom2);

        //when
        waitRoomOnlyOneJoinPolicyService.joinWaitRoomPolicy(hostId);
        joinMemberRedisRepository.save(JoinMember.of(hostId, waitRoom2.getId()));

        //then
        assertThatThrownBy(() -> {
            waitRoomRedisRepository.findById(savedWaitRoom.getId()).orElseThrow(NotExistsRoomIdException::new);
        }).isInstanceOf(NotExistsRoomIdException.class);

        assertThat(joinMemberRedisRepository.findById(hostId).get().getRoomId()).isEqualTo(waitRoom2.getId());


    }

    @Test
    @DisplayName("호스트가 방을 만들고 다른 방에 입장하면, 호스트가 만든 방에 있는 모든 인원의 join 정보를 제거하고 방을 삭제해야 한다.")
    public void joinWaitRoomPolicy_joinOneWaitRoomToHost_andLeaveMembers() throws Exception {
        //given

        for (int i = 0; i < 6; i++) {
            savedWaitRoom.joinMembers(String.valueOf(i));
            joinMemberRedisRepository.save(JoinMember.of(String.valueOf(i), savedWaitRoom.getId()));
        }
        waitRoomRedisRepository.save(savedWaitRoom);

        WaitRoom waitRoom2 = WaitRoom.of(new CreateWaitRoomDto("kose1", "game2", 7), "kose");
        waitRoomRedisRepository.save(waitRoom2);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom2));

        waitRoom2.joinMembers(hostId);
        waitRoomRedisRepository.save(waitRoom2);

        //when
        waitRoomOnlyOneJoinPolicyService.joinWaitRoomPolicy(hostId);
        joinMemberRedisRepository.save(JoinMember.of(hostId, waitRoom2.getId()));

        //then
        assertThatThrownBy(() -> {
            waitRoomRedisRepository.findById(savedWaitRoom.getId()).orElseThrow(NotExistsRoomIdException::new);
        }).isInstanceOf(NotExistsRoomIdException.class);

        assertThat(joinMemberRedisRepository.findById(hostId).get().getRoomId()).isEqualTo(waitRoom2.getId());

    }

}