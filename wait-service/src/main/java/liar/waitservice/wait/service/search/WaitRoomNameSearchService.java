package liar.waitservice.wait.service.search;

import liar.waitservice.wait.service.WaitRoomNameService;
import liar.waitservice.wait.service.WaitRoomService;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WaitRoomNameSearchService implements SearchService {

    private final WaitRoomNameService waitRoomNameService;
    private final WaitRoomService waitRoomService;

    @Override
    public List searchWaitRoomCondition(Object request) {
        return waitRoomNameService.findByIdForSearch((String) request).getRoomIds()
                .stream().map(waitRoomService::findRoomId)
                .map(WaitRoomViewsDto::new).collect(Collectors.toList());
    }
}
