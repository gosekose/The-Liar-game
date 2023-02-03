package liar.waitservice.wait.service.search;

import jakarta.ws.rs.NotFoundException;
import liar.waitservice.wait.controller.dto.SearchWaitRoomDto;
import liar.waitservice.wait.controller.dto.SearchWaitRoomSliceDto;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchConnector {

    private final RoomIdSearchService roomIdSearchService;
    private final RoomNameSearchService roomNameSearchService;
    private final HostNameSearchService hostNameSearchService;

    public List<WaitRoomViewsDto> searchWaitRoomCondition(SearchWaitRoomDto dto) {
        return connectSearchService(dto).searchWaitRoomByCond(dto.getBody());
    }

    public Slice<WaitRoomViewsDto> searchWaitRoomSliceCondition(SearchWaitRoomSliceDto dto) {
        return connectSearchService(dto).searchWaitRoomByCond(dto.getBody(), getPageable(dto));
    }

    private SearchService connectSearchService(SearchWaitRoomDto dto) {
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

    private Pageable getPageable(SearchWaitRoomSliceDto dto) {
        return PageRequest.of(dto.getPage(), dto.getLimit());
    }

}
