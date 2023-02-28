package liar.waitservice.wait.service;

import jakarta.ws.rs.NotFoundException;
import liar.waitservice.wait.MemberDummyInfo;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.WaitRoomCompleteJoinMember;
import liar.waitservice.wait.domain.utils.WaitRoomCompleteStatus;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteRepository;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteJoinMemberRepository;
import liar.waitservice.wait.repository.redis.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import liar.waitservice.wait.service.waitroom.WaitRoomServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WaitRoomServiceImplTest extends MemberDummyInfo {

    @Autowired
    WaitRoomServiceImpl waitRoomCompleteServiceImpl;

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;

    @Autowired
    WaitRoomCompleteJoinMemberRepository waitRoomCompleteJoinMemberRepository;

    @Autowired
    JoinMemberRedisRepository joinMemberRedisRepository;

    WaitRoom waitRoom;
    @Autowired
    private WaitRoomCompleteRepository waitRoomCompleteRepository;

    @BeforeEach
    public void init() {
        waitRoom = WaitRoom.of(new CreateWaitRoomDto(hostId, "koseName", 5), "roomName");
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom));

        waitRoom.joinMembers(devUser1Id);
        waitRoom.joinMembers(devUser2Id);
        waitRoom.joinMembers(devUser2Id);
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(devUser1Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser2Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser3Id, waitRoom.getId()));

    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
        joinMemberRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("waitRoom의 정보를 waitRoomComplete의 엔티티로 변환하여 값을 RDBMS에 저장한다.")
    public void save() throws Exception {
        //given
        waitRoomCompleteServiceImpl.saveWaitRoomComplete(waitRoom);

        //when
        List<WaitRoomComplete> waitRoomCompletes = waitRoomCompleteRepository.findByHostId(hostId);
        List<WaitRoomCompleteJoinMember> waitRoomCompleteJoinMembers = waitRoomCompleteJoinMemberRepository
                .findWaitRoomJoinMemberByWaitRoomComplete(waitRoomCompletes.get(0));

        WaitRoomComplete waitRoomComplete1 = waitRoomCompleteJoinMembers.get(0).getWaitRoomComplete();
        WaitRoomComplete waitRoomComplete2 = waitRoomCompleteJoinMembers.get(1).getWaitRoomComplete();

        //then
        assertThat(waitRoomCompletes.size()).isEqualTo(1);
        assertThat(waitRoomCompleteJoinMembers.size()).isEqualTo(4);
        assertThat(waitRoomComplete1.getHostId()).isEqualTo(waitRoomComplete2.getHostId());
        assertThat(waitRoomComplete1.getHostName()).isEqualTo(waitRoomComplete2.getHostName());
        assertThat(waitRoomComplete1.getWaitRoomId()).isEqualTo(waitRoomComplete2.getWaitRoomId());
        assertThat(waitRoomComplete1.getWaitRoomCompleteStatus()).isEqualTo(waitRoomComplete2.getWaitRoomCompleteStatus());

    }

    @Test
    @DisplayName("종료 사인을 받으면, waitRoomStatus을 END로 변환하여 저장한다.")
    public void updateWaitRoomStatusDueToEndGame() throws Exception {
        //given
        waitRoomCompleteServiceImpl.saveWaitRoomComplete(waitRoom);

        //when
        waitRoomCompleteServiceImpl.updateWaitRoomCompleteStatusEnd(waitRoom.getId());
        WaitRoomComplete waitRoomComplete = waitRoomCompleteRepository
                .findWaitRoomCompleteByWaitRoomId(waitRoom.getId()).orElseThrow(NotFoundException::new);

        //then
        assertThat(waitRoomComplete.getWaitRoomCompleteStatus()).isEqualTo(WaitRoomCompleteStatus.END);
    }

}