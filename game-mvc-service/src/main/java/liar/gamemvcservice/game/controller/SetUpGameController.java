package liar.gamemvcservice.game.controller;

import jakarta.servlet.http.HttpServletRequest;
import liar.gamemvcservice.exception.exception.NotEqualUserIdException;
import liar.gamemvcservice.game.controller.dto.CheckUserRoleDto;
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
    public ResponseEntity setUpGame(SetUpGameDto setUpGameDto) {

        String gameId = gameService.save(setUpGameDto);
        
        return ResponseEntity.ok().body(SendSuccessBody.of(gameId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity checkUserRole(@PathVariable("userId") String userId,
                                        CheckUserRoleDto checkUserRoleDto,
                                        HttpServletRequest request) {

        validateRequestUserEqualHeaderUser(userId, request);

        Player player = gameService.checkPlayerRole(checkUserRoleDto);
        return ResponseEntity.ok().body(SendSuccessBody.of(player));

    }

    private static void validateRequestUserEqualHeaderUser(String userId, HttpServletRequest request) {
        String headerUserId = request.getHeader("userId");
        if (!headerUserId.equals(userId)) {
            throw new NotEqualUserIdException();
        }
    }
}
