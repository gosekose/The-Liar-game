package liar.waitservice.wait.controller;

import liar.waitservice.wait.controller.dto.SearchDto;
import liar.waitservice.wait.service.search.SearchConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wait-service/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchConnector connector;

    @PostMapping("/waitRoom")
    public ResponseEntity searchWaitRooms(SearchDto dto) {
        return ResponseEntity.ok().body(connector.searchWaitRoomCondition(dto));
    }

}
