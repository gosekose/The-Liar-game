package liar.waitservice.wait.controller;

import jakarta.validation.Valid;
import liar.waitservice.wait.controller.dto.UpdateWaitRoomStatusDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.service.start.GameStatusRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wait-service")
@RequiredArgsConstructor
public class UpdateWaitRoomStatusController {

    private final GameStatusRequestService gameStatusRequestService;

    @PostMapping("/start")
    public ResponseEntity saveCache(@Valid @RequestBody RequestWaitRoomDto saveRequest) {
        gameStatusRequestService.saveWaitRoomInfoAtDb(saveRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/end")
    public ResponseEntity deleteCache(@Valid @RequestBody UpdateWaitRoomStatusDto message) {
        gameStatusRequestService.deleteWaitRoomInfoAtCache(message);
        return ResponseEntity.ok().build();
    }

}
