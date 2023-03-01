package liar.waitservice.wait.service.start;

import liar.waitservice.exception.exception.NotEqualHostIdException;
import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.exception.exception.NotSatisfiedMinJoinMembers;
import liar.waitservice.wait.controller.dto.PostProcessEndGameDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.redis.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import liar.waitservice.wait.service.waitroom.WaitRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoProcessStartAndEndGameServiceImpl implements DoProcessStartAndEndGameService<RequestWaitRoomDto, PostProcessEndGameDto<String>> {

    private final WaitRoomService waitRoomService;
    private final WaitRoomRedisRepository waitRoomRedisRepository;
    private final JoinMemberRedisRepository joinMemberRedisRepository;

    @Value("${game.member.limit.min}")
    private int limitMin;

    @Override
    public void doPreProcessBeforeGameStart(RequestWaitRoomDto saveRequest) {
        WaitRoom waitRoom = waitRoomRedisRepository.findById(saveRequest.getRoomId()).orElseThrow(NotFoundWaitRoomException::new);
        waitRoom.updateWaiting();
        WaitRoom savedWaitRoom = waitRoomRedisRepository.save(waitRoom);
        if (isValidated(savedWaitRoom, saveRequest.getUserId())) {
            waitRoomService.saveWaitRoomComplete(savedWaitRoom);
        }
    }

    @Override
    public void doPostProcessAfterGameEnd(PostProcessEndGameDto<String> request) {
        waitRoomService.updateWaitRoomCompleteStatusEnd(request.getRoomId());
        deleteWaitRoomBySuccessGameEndMsg(request);
    }

    /**
     * 게임이 성공적으로 종료된 경우, Redis에 저장된 waitRoom으로 다시 유저 이동
     */
    private boolean deleteWaitRoomBySuccessGameEndMsg(PostProcessEndGameDto<String> message) {
        try{
            WaitRoom waitRoom = waitRoomRedisRepository.findById(message.getRoomId()).orElseThrow(NotFoundWaitRoomException::new);
//            deleteWaitRoomAndJoinMembers(waitRoom);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void deleteWaitRoomAndJoinMembers(WaitRoom waitRoom) {
        waitRoom.getMembers().stream().forEach(j -> joinMemberRedisRepository.delete(new JoinMember(j, waitRoom.getId())));
        waitRoomRedisRepository.delete(waitRoom);
    }

    private boolean isHost(WaitRoom waitRoom, String userId) {
        if (waitRoom.getHostId().equals(userId)) {
            return true;
        }
        throw new NotEqualHostIdException();
    }

    private boolean isMinJoinMembers(WaitRoom waitRoom) {
        if (waitRoom.getMembers().size() >= limitMin ) {
            return true;
        }
        throw new NotSatisfiedMinJoinMembers();
    }

    private boolean isValidated(WaitRoom waitRoom, String userId) {
        if (isHost(waitRoom, userId) && isMinJoinMembers(waitRoom)) {
            return true;
        }
        return false;
    }
}
