package liar.gameservice.game.controller;

import jakarta.validation.Valid;
import liar.gameservice.game.controller.dto.request.GameInfoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;

@RestController
public class SetGameInfoController {

    @PostMapping("/game-service/start/game")
    public Flux<ResponseEntity> setGameInfo(ServerRequest serverRequest,
                                            @Valid @RequestBody GameInfoRequest gameInfoRequest) {

        return null;
    }

}
