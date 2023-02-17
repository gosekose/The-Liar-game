package liar.gamemvcservice.game.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import liar.gamemvcservice.exception.exception.NotEqualUserIdException;
import liar.gamemvcservice.game.controller.dto.RequestCommonDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.controller.dto.message.SendSuccessBody;
import liar.gamemvcservice.game.domain.Player;
import liar.gamemvcservice.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-service/game")
public class SetUpGameController {

    private final GameService gameService;

    @PostMapping("/setup")
    public ResponseEntity setUpGame(@Valid @RequestBody SetUpGameDto setUpGameDto) {

        return ResponseEntity.ok().body(SendSuccessBody.of(gameService.save(setUpGameDto)));
    }

    @GetMapping("/{userId}/role")
    public ResponseEntity checkUserRole(@PathVariable String userId,
                                        @Valid @RequestBody RequestCommonDto dto) {

        return ResponseEntity.ok().body(SendSuccessBody.of(gameService.checkPlayerRole(dto)));
    }

    @GetMapping("/{userId}/topic")
    public ResponseEntity checkTopic(@PathVariable String userId,
                                     @Valid @RequestBody RequestCommonDto dto) {

        return ResponseEntity.ok().body(SendSuccessBody.of(gameService.checkTopic(dto).getTopicName()));
    }

    @PostMapping("/{userId}/turn")
    public ResponseEntity getPlayerTurn(@PathVariable String userId,
                                        @Valid @RequestBody RequestCommonDto dto) {

        return ResponseEntity.ok().body(SendSuccessBody.of(gameService.setUpTurn(dto.getGameId())));
    }

}
