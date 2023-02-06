package liar.gameservice.game.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/game-service/game")
public class PlayGameStartAndEndController {

    @PostMapping("/start")
    public Flux<String> startGame() {
        return Flux.just("Example1");
    }

}
