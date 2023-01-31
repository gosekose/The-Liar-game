package liar.waitservice.wait.service.search;

import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.service.WaitRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WaitRoomSearchService implements SearchService {

    private final WaitRoomService waitRoomService;

    @Override
    public List searchWaitRoomCondition(Object request) {
        WaitRoom waitRoom = waitRoomService.findRoomId((String) request);
        return Arrays.asList(waitRoom);
    }
}
