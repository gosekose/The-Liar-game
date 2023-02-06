package liar.waitservice.wait.controller;

import jakarta.validation.Valid;
import liar.waitservice.wait.controller.dto.UpdateWaitRoomStatusDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.service.start.UpdateWaitRoomStatusService;
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

    private final UpdateWaitRoomStatusService updateWaitRoomStatusService;

    @PostMapping("/start/game")
    public ResponseEntity saveCache(@Valid @RequestBody RequestWaitRoomDto saveRequest) {
        updateWaitRoomStatusService.saveWaitRoomInfoAtDb(saveRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/end/game")
    public ResponseEntity deleteCache(@Valid @RequestBody UpdateWaitRoomStatusDto message) {
        updateWaitRoomStatusService.deleteWaitRoomInfoAtCache(message);
        return ResponseEntity.ok().build();
    }

}
