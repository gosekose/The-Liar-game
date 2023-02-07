package liar.gameservice.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
@RequiredArgsConstructor
public class WebClientUtil {

    public WebClient build(ServerRequest request) {

        return WebClient.builder()
                .defaultHeader("Authorization", request.headers().header("Authorization").get(0))
                .defaultHeader("RefreshToken", request.headers().header("RefreshToken").get(0))
                .defaultHeader("userId", request.headers().header("userId").get(0))
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
