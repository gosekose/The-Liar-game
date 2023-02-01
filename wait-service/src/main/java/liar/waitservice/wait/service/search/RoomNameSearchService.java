package liar.waitservice.wait.service.search;

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
public class RoomNameSearchService implements SearchService<WaitRoomViewsDto, String> {

    private final WaitRoomService waitRoomService;

    @Override
    public List<WaitRoomViewsDto> searchWaitRoomByCond(String request) {
        return waitRoomService.findWaitRoomByRoomName((String) request)
                .stream()
                .map(WaitRoomViewsDto::new)
                .collect(Collectors.toList());
    }
}
