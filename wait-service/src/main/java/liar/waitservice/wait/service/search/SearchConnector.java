package liar.waitservice.wait.service.search;

import jakarta.ws.rs.NotFoundException;
import liar.waitservice.wait.controller.dto.SearchDto;
import liar.waitservice.wait.service.OnlineHostService;
import liar.waitservice.wait.service.WaitRoomNameService;
import liar.waitservice.wait.service.WaitRoomService;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchConnector {

    private final WaitRoomSearchService waitRoomSearchService;
    private final WaitRoomNameSearchService waitRoomNameSearchService;
    private final OnlineHostSearchService onlineHostSearchService;

    public List<WaitRoomViewsDto> searchWaitRoomCondition(SearchDto dto) {
        return connectSearchService(dto).searchWaitRoomCond(dto.getRequest());
    }

    private SearchService connectSearchService(SearchDto dto) {
        switch (dto.upperSearchType()) {

            case "WAITROOMID":
                return waitRoomSearchService;

            case "WAITROOMNAME":
                return waitRoomNameSearchService;

            case "HOSTNAME":
                return onlineHostSearchService;

            default:
                throw new NotFoundException();
        }
    }

}
