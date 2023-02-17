package liar.gamemvcservice.game.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import liar.gamemvcservice.game.controller.dto.VoteLiarDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/game-service/game")
public class VoteLiarController {

    @PostMapping("/{userId}/vote")
    public ResponseEntity voteLiar(@PathVariable String userId,
                                   @Valid @RequestBody VoteLiarDto dto,
                                   HttpServletRequest request) {
        return null;
    }
}
