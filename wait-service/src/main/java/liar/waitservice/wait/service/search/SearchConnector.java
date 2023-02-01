package liar.waitservice.wait.service.search;

import jakarta.ws.rs.NotFoundException;
import liar.waitservice.wait.controller.dto.SearchDto;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchConnector {

    private final RoomIdSearchService roomIdSearchService;
    private final RoomNameSearchService roomNameSearchService;
    private final HostNameSearchService hostNameSearchService;

    public List<WaitRoomViewsDto> searchWaitRoomCondition(SearchDto dto) {
        return connectSearchService(dto).searchWaitRoomByCond(dto.getRequest());
    }

    private SearchService connectSearchService(SearchDto dto) {
        switch (dto.upperSearchType()) {

            case "WAITROOMID":
                return roomIdSearchService;

            case "WAITROOMNAME":
                return roomNameSearchService;

            case "HOSTNAME":
                return hostNameSearchService;

            default:
                throw new NotFoundException();
        }
    }

}
