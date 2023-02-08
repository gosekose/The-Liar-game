package liar.gameservice.game.controller;

import jakarta.validation.Valid;
import liar.gameservice.game.controller.dto.request.SetRoleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-service/game")
public class SetRoleController {

    private final HttpEntityUtil httpEntityUtil;
    private final WebClientUtil webClientUtil;
    private final WebClient webClient;

    @PostMapping(value = "/game-service/game/{roomId}/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> setRole(
            @PathVariable(name = "roomId") String roomId,
            @Valid @RequestBody SetRoleRequest request) {

        return Mono.just(ResponseEntity.ok().body("test"));
    }

    //     wait-service에서 바로 처리
//        ResponseEntity<WaitServiceStartGameResponse> waitServiceResponse = restTemplate
//                .postForEntity("http://localhost:8081/wait-service/game/start",
//                        httpEntityUtil.getHttpEntity(serverRequest, request), WaitServiceStartGameResponse.class);
//                    return webClientUtil.build(serverRequest)
//            .post().uri("http://localhost:8081/wait-service/game/start")
//                .contentType(APPLICATION_JSON)
//                .body(BodyInserters.fromValue(request))
//            .retrieve()
//                .toEntity(CustomBodyResponse.class);
}
