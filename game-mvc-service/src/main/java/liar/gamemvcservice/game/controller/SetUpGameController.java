package liar.gamemvcservice.game.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import liar.gamemvcservice.exception.exception.NotEqualUserIdException;
import liar.gamemvcservice.game.controller.dto.GameUserInfoDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.controller.dto.message.SendSuccessBody;
import liar.gamemvcservice.game.domain.Player;
import liar.gamemvcservice.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-service/game")
public class SetUpGameController {

    private final GameService gameService;

    @PostMapping("/setup")
    public ResponseEntity setUpGame(@Valid @RequestBody SetUpGameDto setUpGameDto) {

        String gameId = gameService.save(setUpGameDto);

        return ResponseEntity.ok().body(SendSuccessBody.of(gameId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity checkUserRole(@PathVariable("userId") String userId,
                                        @Valid @RequestBody GameUserInfoDto dto,
                                        HttpServletRequest request) {

        validateRequestUserEqualHeaderUser(userId, dto, request);
        return ResponseEntity.ok().body(SendSuccessBody.of(gameService.checkPlayerRole(dto)));
    }

    @GetMapping("/{userId}/topic")
    public ResponseEntity checkTopic(@PathVariable("userId") String userId,
                                     @Valid @RequestBody GameUserInfoDto dto,
                                     HttpServletRequest request) {

        validateRequestUserEqualHeaderUser(userId, dto, request);
        return ResponseEntity.ok().body(SendSuccessBody.of(gameService.checkTopic(dto)));
    }

    private static void validateRequestUserEqualHeaderUser(String userId, GameUserInfoDto dto,
                                                           HttpServletRequest request) {
        String headerUserId = request.getHeader("userId");
        if (!(headerUserId.equals(userId) && dto.getUserId().equals(userId))) {
            throw new NotEqualUserIdException();
        }
    }
}
