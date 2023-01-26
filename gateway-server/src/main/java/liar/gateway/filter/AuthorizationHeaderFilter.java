package liar.gateway.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotAuthorizedException;
import liar.gateway.domain.TokenProviderImpl;
import liar.gateway.exception.exception.NotAuthorizationCustomException;
import liar.gateway.repository.TokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
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
            log.info("request.getHeaders() = {}", request.getHeaders());
            log.info("request.getURI() = {}", request.getURI());
            log.info("request.getPath() = {}", request.getPath());

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new NotAuthorizationCustomException();
            }

            String jwt = resolveToken(request);
            String requestURI = request.getURI().getPath();

            if (StringUtils.hasText(jwt)
                            && tokenProviderImpl.validateToken(jwt)
                            && isNotLogoutAccessToken(jwt)) {
                return chain.filter(exchange);
            }

            return onError(exchange, "Error", HttpStatus.BAD_REQUEST);
        };
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().get(AUTHORIZATION_HEADER).get(0);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    /**
     *
     * token이 Logout한 AccessToken이라면 , false 출력
     *
     * @param token
     * @return
     */
    private boolean isNotLogoutAccessToken(String token) {
        return !tokenRepository.existsLogoutAccessTokenById(token);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }

}
