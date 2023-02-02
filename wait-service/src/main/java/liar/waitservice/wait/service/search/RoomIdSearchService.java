package liar.waitservice.wait.service.search;

import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.service.WaitRoomService;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomIdSearchService implements SearchService<WaitRoomViewsDto, String> {

    private final WaitRoomService waitRoomService;

    @Override
    public List<WaitRoomViewsDto> searchWaitRoomByCond(String body) {
        WaitRoom waitRoom = waitRoomService.findWaitRoomId(body);
        return Arrays.asList(waitRoom).stream().map(WaitRoomViewsDto::new).collect(Collectors.toList());
    }


}
