package liar.waitservice.wait.service.start;

import jakarta.ws.rs.NotFoundException;
import liar.waitservice.exception.exception.NotEqualHostIdException;
import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.exception.exception.NotSatisfiedMinJoinMembers;
import liar.waitservice.wait.MemberDummyInfo;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.PostProcessEndGameDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.utils.WaitRoomCompleteStatus;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DoProcessStartAndEndGameServiceImplTest extends MemberDummyInfo {

    @Autowired
    WaitRoomCompleteRepository waitRoomCompleteRepository;

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;

    @Autowired
    JoinMemberRedisRepository joinMemberRedisRepository;

    @Autowired
    DoProcessStartAndEndGameServiceImpl updateWaitRoomStatusService;

    @Autowired
    WaitRoomServiceImpl waitRoomCompleteServiceImpl;

    WaitRoom waitRoom;

    @BeforeEach
    public void init() {
        waitRoom = WaitRoom.of(new CreateWaitRoomDto(hostId, "koseRoom", 5), "koseName");
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom));
    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
        joinMemberRedisRepository.deleteAll();
    }
    
    @Test
    @DisplayName("RequestWaitRoomDto??? ????????? ????????? ???????????? ?????? ????????? ????????????, redis??? ????????? waitRoom??? WaitRoomComplete??? RDMBS??? ????????????")
    public void doPreProcessBeforeGameStart_success() throws Exception {

        //given
        waitRoom.joinMembers(devUser1Id);
        waitRoom.joinMembers(devUser2Id);
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(devUser1Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser2Id, waitRoom.getId()));

        RequestWaitRoomDto requestWaitRoomDto = new RequestWaitRoomDto(waitRoom.getHostId(), waitRoom.getId());

        //when
        updateWaitRoomStatusService.doPreProcessBeforeGameStart(requestWaitRoomDto);
        WaitRoomComplete waitRoomComplete = waitRoomCompleteRepository.findWaitRoomCompleteByWaitRoomId(waitRoom.getId()).orElseThrow(NotFoundException::new);

        //then
        assertThat(waitRoomComplete.getHostName()).isEqualTo(waitRoom.getHostName());
        assertThat(waitRoomComplete.getHostId()).isEqualTo(waitRoom.getHostId());
        assertThat(waitRoomComplete.getLimitMembers()).isEqualTo(waitRoom.getLimitMembers());
        assertThat(waitRoomComplete.getWaitRoomCompleteStatus()).isEqualTo(WaitRoomCompleteStatus.PLAYING);

    }

    @Test
    @DisplayName("???????????? ????????????, doPreProcessBeforeGameStart ????????? ????????????.")
    public void doPreProcessBeforeGameStart_fail_becauseNotHost() throws Exception {

        //given
        waitRoom.joinMembers(devUser1Id);
        waitRoom.joinMembers(devUser2Id);
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(devUser1Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser2Id, waitRoom.getId()));

        //when
        RequestWaitRoomDto requestWaitRoomDto = new RequestWaitRoomDto(devUser1Id, waitRoom.getId());

        //then
        assertThatThrownBy(() -> updateWaitRoomStatusService.doPreProcessBeforeGameStart(requestWaitRoomDto))
                .isInstanceOf(NotEqualHostIdException.class);

    }

    @Test
    @DisplayName("?????? ????????? ???????????? ?????????, doPreProcessBeforeGameStart ????????? ????????????.")
    public void doPreProcessBeforeGameStart_fail_becauseNotSatisfiedjoinMembers() throws Exception {
        //given
        waitRoom.joinMembers(devUser1Id);
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(devUser1Id, waitRoom.getId()));

        // when
        RequestWaitRoomDto requestWaitRoomDto = new RequestWaitRoomDto(waitRoom.getHostId(), waitRoom.getId());

        //then
        assertThatThrownBy(() -> updateWaitRoomStatusService.doPreProcessBeforeGameStart(requestWaitRoomDto))
                .isInstanceOf(NotSatisfiedMinJoinMembers.class);

    }

    @Test
    @DisplayName("????????? ????????? ????????? ?????????, doPreProcessBeforeGameStart ????????? ????????????.")
    public void doPreProcessBeforeGameStart_fail_becauseNotFoundRoom() throws Exception {

        //given
        waitRoom.joinMembers(devUser1Id);
        waitRoom.joinMembers(devUser2Id);
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(devUser1Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser2Id, waitRoom.getId()));

        //when
        RequestWaitRoomDto requestWaitRoomDto = new RequestWaitRoomDto(hostId, "???");

        //then
        assertThatThrownBy(() -> updateWaitRoomStatusService.doPreProcessBeforeGameStart(requestWaitRoomDto))
                .isInstanceOf(NotFoundWaitRoomException.class);

    }

    @Test
    @DisplayName("deleteWaitRoomFromRedis??? redis??? ?????? waitRoom??? joinMembers??? ????????? ????????????, waitRoomComplete ????????? End??? ????????????.")
    public void doPreProcessBeforeGameStart() throws Exception {
        //given
        waitRoom.joinMembers(devUser1Id);
        waitRoom.joinMembers(devUser2Id);
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(devUser1Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser2Id, waitRoom.getId()));

        RequestWaitRoomDto requestWaitRoomDto = new RequestWaitRoomDto(hostId, waitRoom.getId());
        updateWaitRoomStatusService.doPreProcessBeforeGameStart(requestWaitRoomDto);


        //when
        PostProcessEndGameDto postProcessEndGameDto = new PostProcessEndGameDto<String>(waitRoom.getId());
        updateWaitRoomStatusService.doPostProcessAfterGameEnd(postProcessEndGameDto);
        WaitRoomComplete waitRoomComplete = waitRoomCompleteServiceImpl.findWaitRoomCompleteByWaitRoomId(waitRoom.getId());

        //then
        assertThat(waitRoomComplete.getWaitRoomCompleteStatus()).isEqualTo(WaitRoomCompleteStatus.END);
        assertThatThrownBy(() -> joinMemberRedisRepository.findById(hostId).orElseThrow(NotFoundException::new));
        assertThatThrownBy(() -> joinMemberRedisRepository.findById(devUser1Id).orElseThrow(NotFoundException::new));
        assertThatThrownBy(() -> joinMemberRedisRepository.findById(devUser2Id).orElseThrow(NotFoundException::new));
        assertThatThrownBy(() -> waitRoomRedisRepository.findById(waitRoom.getId()).orElseThrow(NotFoundException::new));
    }





}