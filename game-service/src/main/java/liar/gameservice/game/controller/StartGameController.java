package liar.gameservice.game.controller;

import jakarta.validation.Valid;
import liar.gameservice.game.controller.dto.request.StartGameRequest;
import liar.gameservice.game.controller.dto.response.CustomBodyResponse;
import liar.gameservice.game.controller.dto.response.WaitServiceStartGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-service/game")
public class StartGameController {

    private final HttpEntityUtil httpEntityUtil;
    private final WebClientUtil webClient;
    private final RestTemplate restTemplate;

    @PostMapping("/start")
    public Mono<ResponseEntity> startGame(
            ServerRequest serverRequest,
            @Valid @RequestBody StartGameRequest request) {

        ResponseEntity<WaitServiceStartGameResponse> waitServiceResponse = restTemplate
                .postForEntity("http://localhost:8081/wait-service/game/start",
                        httpEntityUtil.getHttpEntity(serverRequest, request), WaitServiceStartGameResponse.class);

        // TO Do
        // game-service

        return webClient.build(serverRequest)
                .post().uri("http://localhost:8081/wait-service/game/start")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .toEntity(CustomBodyResponse.class);
    }

}
