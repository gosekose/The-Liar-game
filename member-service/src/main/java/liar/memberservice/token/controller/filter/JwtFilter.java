package liar.memberservice.token.controller.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import liar.memberservice.token.domain.TokenProviderImpl;
import liar.memberservice.token.repository.TokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProviderImpl tokenProviderImpl;
    private final TokenRepositoryImpl tokenRepository;

    private static final String[] whitelist = {
            "/",
            "/static/**",
            "/favicon.ico",
            "/member-service/login",
            "/member-service/register",
            "/member-service/test",
            "/oauth2/authorization/google",
            "/oauth2/authorization/naver",
            "/oauth2/authorization/kakao"
    };

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
