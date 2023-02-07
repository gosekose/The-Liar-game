package liar.gameservice.game.controller;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
public class HttpEntityUtil<T> {

    public HttpEntity<MultivaluedMap> getHttpEntity(ServerRequest serverRequest, T t) {
        HttpHeaders headers = getHttpHeaders(serverRequest);
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                params.add(field.getName(), field.get(t).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return new HttpEntity<>(params, headers);
    }

    @NotNull
    private static HttpHeaders getHttpHeaders(ServerRequest serverRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", serverRequest.headers().header("Authorization").get(0));
        headers.set("Authorization", serverRequest.headers().header("RefreshToken").get(0));
        headers.set("Authorization", serverRequest.headers().header("userId").get(0));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
