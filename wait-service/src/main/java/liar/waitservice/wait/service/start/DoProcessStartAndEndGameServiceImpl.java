package liar.waitservice.wait.service.start;

import liar.waitservice.exception.exception.NotEqualHostIdException;
import liar.waitservice.exception.exception.NotSatisfiedMinJoinMembers;
import liar.waitservice.wait.controller.dto.PostProcessEndGameDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.service.WaitRoomCompleteService;
import liar.waitservice.wait.service.WaitRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoProcessStartAndEndGameServiceImpl implements DoProcessStartAndEndGameService<RequestWaitRoomDto, PostProcessEndGameDto<String>> {

    private final WaitRoomCompleteService waitRoomCompleteService;
    private final WaitRoomService waitRoomService;

    @Override
    public void doPreProcessBeforeGameStart(RequestWaitRoomDto saveRequest) {
        WaitRoom waitRoom = waitRoomService.findWaitRoomId(saveRequest.getRoomId());
        if (isValidated(waitRoom, saveRequest.getUserId())) {
            waitRoomCompleteService.save(waitRoom);
        }
    }

    @Override
    public void doPostProcessAfterGameEnd(PostProcessEndGameDto<String> request) {
        waitRoomCompleteService.updateWaitRoomCompleteStatusEnd(request.getRoomId());
        waitRoomService.deleteWaitRoomBySuccessGameEndMsg(request);
    }

    private boolean isHost(WaitRoom waitRoom, String userId) {
        if (waitRoom.getHostId().equals(userId)) {
            return true;
        }
        throw new NotEqualHostIdException();
    }

    private boolean isMinJoinMembers(WaitRoom waitRoom) {
        if (waitRoom.getMembers().size() >= 3 ) {
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
