package liar.gamemvcservice.game.controller;

import jakarta.validation.Valid;
import liar.gamemvcservice.game.controller.dto.message.SendSuccessBody;
import liar.gamemvcservice.game.controller.dto.request.VoteLiarRequest;
import liar.gamemvcservice.game.service.GameFacadeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/game-service/game")
public class VoteLiarController {

    private final GameFacadeServiceImpl gameFacadeServiceImpl;

    @PostMapping("/{userId}/vote")
    public ResponseEntity voteLiar(@PathVariable String userId,
                                   @Valid @RequestBody VoteLiarRequest dto) throws InterruptedException {
        return ResponseEntity.ok().body(SendSuccessBody.of(gameFacadeServiceImpl
                .voteLiarUser(dto)));
    }
}
