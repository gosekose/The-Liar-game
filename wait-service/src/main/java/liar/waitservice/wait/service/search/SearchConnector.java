package liar.waitservice.wait.service.search;

import liar.waitservice.wait.controller.dto.SearchDto;
import liar.waitservice.wait.service.OnlineHostService;
import liar.waitservice.wait.service.WaitRoomNameService;
import liar.waitservice.wait.service.WaitRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchConnector<T> {

    private final WaitRoomService waitRoomService;
    private final WaitRoomNameService waitRoomNameService;
    private final OnlineHostService onlineHostService;

    private SearchService connectSearchService(SearchDto dto) {
        switch (dto.upperSearchType()) {
            case "WAITROOMID":
                return null;
            case "WAITROOMNAME":
                return null;
            case "HOSTNAME":
                return null;
            default:
                return null;
        }
    }

    public List<T> searchWaitRoomCondition(SearchDto dto) {
        return connectSearchService(dto).searchWaitRoomCondition(dto.getRequest());
    }

}
