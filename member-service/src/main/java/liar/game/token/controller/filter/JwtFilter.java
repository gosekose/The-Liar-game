package liar.game.token.controller.filter;

import io.jsonwebtoken.Claims;
import liar.game.token.domain.TokenProviderImpl;
import liar.game.token.repository.TokenRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String[] whitelist = {
            "/",
            "/static/**",
            "/favicon.ico",
            "/api/v1/login",
            "/api/v1/register",
            "/oauth2/authorization/google",
            "/oauth2/authorization/naver",
            "/oauth2/authorization/kakao"
    };

    private final TokenProviderImpl tokenProviderImpl;
    private final TokenRepositoryImpl tokenRepository;

    public JwtFilter(TokenProviderImpl tokenProviderImpl, TokenRepositoryImpl tokenRepository) {
        this.tokenProviderImpl = tokenProviderImpl;

        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();

        if (isLoginCheckPath(requestURI)) {

            if (
                    StringUtils.hasText(jwt)
                    && tokenProviderImpl.validateToken(jwt)
                    && isNotLogoutAccessToken(jwt)
            ) {
//                Claims claims = tokenProviderImpl.getClaims(jwt);
                Authentication authentication = tokenProviderImpl.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                return ;
            }

        }

        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
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

}
