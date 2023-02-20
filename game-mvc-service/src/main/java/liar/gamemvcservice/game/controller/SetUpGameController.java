package liar.gamemvcservice.game.controller;

import jakarta.validation.Valid;
import liar.gamemvcservice.game.controller.dto.request.CommonRequest;
import liar.gamemvcservice.game.service.dto.CommonDto;
import liar.gamemvcservice.game.controller.dto.request.SetUpGameRequest;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import liar.gamemvcservice.game.controller.dto.message.SendSuccessBody;
import liar.gamemvcservice.game.service.GameFacadeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-service/game")
public class SetUpGameController {

    private final GameFacadeServiceImpl gameFacadeServiceImpl;

    @PostMapping("/setup")
    public ResponseEntity setUpGame(@Valid @RequestBody SetUpGameRequest request) {
        return ResponseEntity.ok().body(SendSuccessBody.of(gameFacadeServiceImpl.save(SetUpGameDto.of(request))));
    }

    @GetMapping("/{userId}/role")
    public ResponseEntity checkUserRole(@PathVariable String userId,
                                        @Valid @RequestBody CommonRequest request) {
        return ResponseEntity.ok().body(SendSuccessBody.of(gameFacadeServiceImpl.checkPlayerRole(CommonDto.of(request))));
    }

    @GetMapping("/{userId}/topic")
    public ResponseEntity checkTopic(@PathVariable String userId,
                                     @Valid @RequestBody CommonRequest request) {
        return ResponseEntity.ok()
                .body(SendSuccessBody.of(gameFacadeServiceImpl.checkTopic(CommonDto.of(request)).getTopicName()));
    }

    @PostMapping("/{userId}/turn")
    public ResponseEntity getPlayerTurn(@PathVariable String userId,
                                        @Valid @RequestBody CommonRequest request) {
        return ResponseEntity
                .ok().body(SendSuccessBody.of(gameFacadeServiceImpl.setUpTurn(CommonDto.of(request).getGameId())));
    }

}
