package liar.waitservice.wait.controller;

import jakarta.validation.Valid;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.controller.dto.message.*;
import liar.waitservice.wait.service.WaitRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wait-service")
@RequiredArgsConstructor
public class CrudWaitRoomController {

    private final WaitRoomService waitRoomService;

    @PostMapping("/waitroom/create")
    public ResponseEntity createWaitRoom(@Valid @RequestBody CreateWaitRoomDto dto) {
        String waitRoomId = waitRoomService.saveWaitRoomByHost(dto);
        return ResponseEntity.ok().body(SendSuccessRoomId.of(waitRoomId));
    }

    @PostMapping("/waitroom/delete")
    public ResponseEntity deleteWaitRoom(@Valid @RequestBody RequestWaitRoomDto dto) {
        boolean deleteStatus = waitRoomService.deleteWaitRoomByHost(dto);
        return ResponseEntity.ok().body(SendSuccessDelete.of(deleteStatus));
    }

    @PostMapping("/waitroom/join")
    public ResponseEntity joinMember(@Valid @RequestBody RequestWaitRoomDto dto) {
        boolean joinStatus = waitRoomService.addMembers(dto);
        return ResponseEntity.ok().body(SendSuccessJoin.of(joinStatus));
    }

    @PostMapping("/waitroom/leave")
    public ResponseEntity leaveMember(@Valid @RequestBody RequestWaitRoomDto dto) {
        boolean leaveStatus = waitRoomService.leaveMember(dto);
        return ResponseEntity.ok().body(SendSuccessLeave.of(leaveStatus));
    }

}
