package liar.waitservice.wait.service.start;

import liar.waitservice.exception.exception.NotEqualHostIdException;
import liar.waitservice.wait.controller.dto.UpdateWaitRoomStatusDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.service.WaitRoomCompleteService;
import liar.waitservice.wait.service.WaitRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateWaitRoomStatusServiceImpl implements UpdateWaitRoomStatusService<RequestWaitRoomDto, UpdateWaitRoomStatusDto<String>> {

    private final WaitRoomCompleteService waitRoomCompleteService;
    private final WaitRoomService waitRoomService;

    @Override
    public void saveWaitRoomInfoAtDb(RequestWaitRoomDto saveRequest) {
        WaitRoom waitRoom = waitRoomService.findWaitRoomId(saveRequest.getRoomId());
        isHost(waitRoom, saveRequest.getUserId());
        waitRoomCompleteService.save(waitRoom);
    }

    @Override
    public void deleteWaitRoomInfoAtCache(UpdateWaitRoomStatusDto<String> request) {
        waitRoomCompleteService.updateWaitRoomCompleteStatusEnd(request.getRoomId());
        waitRoomService.deleteWaitRoomBySuccessGameEndMsg(request);
    }

    private boolean isHost(WaitRoom waitRoom, String userId) {
        if (waitRoom.getHostId().equals(userId)) {
            return true;
        }
        throw new NotEqualHostIdException();
    }
}
