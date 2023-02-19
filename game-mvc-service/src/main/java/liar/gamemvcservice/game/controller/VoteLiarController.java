package liar.gamemvcservice.game.controller;

import jakarta.validation.Valid;
import liar.gamemvcservice.game.controller.dto.VoteLiarRequest;
import liar.gamemvcservice.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/game-service/game")
public class VoteLiarController {

    private final GameService gameService;

    @PostMapping("/{userId}/vote")
    public ResponseEntity voteLiar(@PathVariable String userId,
                                   @Valid @RequestBody VoteLiarRequest dto) {

        return null;
    }
}
