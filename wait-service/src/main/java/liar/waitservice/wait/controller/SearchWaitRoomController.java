package liar.waitservice.wait.controller;

import jakarta.validation.Valid;
import liar.waitservice.wait.controller.dto.SearchWaitRoomDto;
import liar.waitservice.wait.controller.dto.message.SendSuccessBody;
import liar.waitservice.wait.service.search.SearchConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wait-service/search")
@RequiredArgsConstructor
public class SearchWaitRoomController {

    private final SearchConnector connector;

    @PostMapping("/waitroom")
    public ResponseEntity searchWaitRooms(@Valid @RequestBody SearchWaitRoomDto dto) {
        return ResponseEntity.ok().body(SendSuccessBody.of(connector.searchWaitRoomCondition(dto)));
    }

}
