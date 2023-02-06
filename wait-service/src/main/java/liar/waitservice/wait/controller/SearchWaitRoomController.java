package liar.waitservice.wait.controller;

import jakarta.validation.Valid;
import liar.waitservice.wait.controller.dto.SearchWaitRoomDto;
import liar.waitservice.wait.controller.dto.SearchWaitRoomSliceDto;
import liar.waitservice.wait.controller.dto.message.SendSuccessBody;
import liar.waitservice.wait.service.search.SearchConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wait-service")
@RequiredArgsConstructor
public class SearchWaitRoomController {

    private final SearchConnector connector;

    @GetMapping("/waitroom/search")
    public ResponseEntity searchWaitRooms(@Valid @RequestBody SearchWaitRoomDto dto) {
        return ResponseEntity.ok().body(SendSuccessBody.of(connector.searchWaitRoomCondition(dto)));
    }

    @GetMapping("/waitroom-slice/search")
    public ResponseEntity searchWaitRoomsSlice(@Valid @RequestBody SearchWaitRoomSliceDto dto) {
        return ResponseEntity.ok().body(SendSuccessBody.of(connector.searchWaitRoomSliceCondition(dto)));
    }
}
