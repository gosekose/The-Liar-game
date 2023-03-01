package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.other.dao.MemberNameOnly;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.PostProcessEndGameDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.WaitRoomCompleteJoinMember;
import org.springframework.stereotype.Service;

@Service
public interface WaitRoomFacadeService {

    void save(WaitRoom waitRoom);

    void updateWaitRoomCompleteStatusEnd(String roomId);

    WaitRoomComplete findWaitRoomCompleteByWaitRoomId(String roomId);


    void doPreProcessBeforeGameStart(RequestWaitRoomDto saveRequest);

    void doPostProcessAfterGameEnd(PostProcessEndGameDto<String> request);


    /**
     * waitRoom을 저장
     * createWaitRoomDto로 waitRoom의 정보를 얻고, userId로 hostName 불러오기
     * waitRoom을 redis에 저장하고, joinMembers를 생성하여 저장한다.
     */
    String saveWaitRoomByHost(CreateWaitRoomDto createWaitRoomDto);
    /**
     * 호스트가 아닌 다른 유저 대기방 요청 승인
     * 게임이 진행 중이거나 현재 게임 중인 유저인 경우, 현재 게임에 참여할 수 없음.
     */
    boolean addMembers(RequestWaitRoomDto dto);

    /**
     * 호스트가 아닌 다른 유저 대기방 나가기
     */
    boolean leaveMember(RequestWaitRoomDto requestWaitRoomDto);

    /**
     * 대기방 탈퇴 요청이 호스트라면, 대기방에 참여한 인원의 join key를 삭제하고, 방의 정보 전체 삭제
     */
    boolean deleteWaitRoomByHost(RequestWaitRoomDto request);
}
