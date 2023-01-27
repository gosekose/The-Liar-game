package liar.gateway.filter;

import liar.gateway.domain.TokenProviderImpl;
import liar.gateway.exception.exception.NotUserIdHeaderException;
import liar.gateway.exception.exception.NotAuthorizationHeaderException;
import liar.gateway.repository.TokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProviderImpl tokenProviderImpl;
    private final TokenRepositoryImpl tokenRepository;

//    private static final String[] whitelist = {
//            "/",
//            "/static/**",
//            "/favicon.ico",
//            "/member-service/login",
//            "/member-service/register",
//            "/member-service/test",
//            "/oauth2/authorization/google",
//            "/oauth2/authorization/naver",
//            "/oauth2/authorization/kakao"
//    };

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            hasAuthorizationHeader(request);
            hasUserIdHeader(request);

            String jwt = parseToken(request);
            String userId = parseUserId(request);

            if (StringUtils.hasText(jwt)
                            && tokenProviderImpl.validateToken(jwt, userId)
                            && isNotLogoutAccessToken(jwt)) {
                return chain.filter(exchange);
            }
            return onError(exchange, "Error", HttpStatus.BAD_REQUEST);
        };
    }


    /**
     * request 요청에서 token 파싱
     */
    private String parseToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().get(AUTHORIZATION_HEADER).get(0);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * request 요청에서 userId 파싱
     */
    public String parseUserId(ServerHttpRequest request) {
        return request.getHeaders().get("userId").get(0);
    }

    /**
     * token이 Logout한 AccessToken이라면 , false 출력
     */
    private boolean isNotLogoutAccessToken(String token) {
        return !tokenRepository.existsLogoutAccessTokenById(token);
    }

    /**
     * Authorization 헤더 포함 및 header List empty 여부 확인
     */
    private static void hasAuthorizationHeader(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION) ||
                request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).isEmpty()) {
            throw new NotAuthorizationHeaderException();
        }

    }

    /**
     * userId 헤더 포함 및 header List empty 여부 확인
     */
    private static void hasUserIdHeader(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey("userId") ||
                request.getHeaders().get("userId").get(0).isEmpty()) {
            throw new NotUserIdHeaderException();
        }
    }

    /**
     * Error Log
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }

}
