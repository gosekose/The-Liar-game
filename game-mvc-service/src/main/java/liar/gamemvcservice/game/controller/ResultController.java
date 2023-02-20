package liar.gamemvcservice.game.controller;

import liar.gamemvcservice.game.controller.dto.message.SendSuccessBody;
import liar.gamemvcservice.game.controller.dto.request.CommonRequest;
import liar.gamemvcservice.game.service.GameFacadeServiceImpl;
import liar.gamemvcservice.game.service.dto.GameResultToClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("game-service/game")
public class ResultController {

    private final GameFacadeServiceImpl gameFacadeServiceImpl;

    @GetMapping("/result")
    public ResponseEntity getGameResultInformation(CommonRequest dto) {

        GameResultToClientDto gameResultToClientDto = gameFacadeServiceImpl
                .messageGameResultToClient(dto.getGameId());


        return ResponseEntity.ok()
                .body(SendSuccessBody.of(gameResultToClientDto));
    }

}
