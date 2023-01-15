package liar.game.token.service;

import liar.game.common.exception.exception.NotExistsRefreshTokenException;
import liar.game.member.domain.Authority;
import liar.game.token.domain.LogoutAccessToken;
import liar.game.token.domain.LogoutRefreshToken;
import liar.game.token.domain.RefreshToken;
import liar.game.token.domain.TokenProviderImpl;
import liar.game.token.repository.RefreshTokenRedisRepository;
import liar.game.token.repository.TokenRepository;
import liar.game.token.controller.dto.TokenAuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final TokenProviderImpl tokenProviderImpl;
    private final TokenRepository tokenRepository;

    /**
     *
     * controller에서 검증 후
     * Id/password 이메일 방식에 대한 토큰 발급
     *
     */
    @Transactional
    public TokenAuthDto createFormTokenAuth(String email, List<Authority> authorities) {

        String accessToken = tokenProviderImpl.createAccessToken(email, authorities);
        String refreshToken = createFormNewRefreshToken(email, authorities);

        log.info("accessToken = {}", accessToken);
        log.info("refreshToken = {}", refreshToken);

        return new TokenAuthDto(accessToken, refreshToken);
    }

    /**
     *
     * controller에서 검증 후
     * oAuth2 이메일 방식에 대한 토큰 발급
     *
     */
    @Transactional
    public TokenAuthDto createOauthTokenAuth(Authentication authentication) {

        String accessToken = tokenProviderImpl.createAccessToken(authentication);
        String newRefreshToken = createOauth2NewRefreshToken(authentication);

        log.info("accessToken = {}", accessToken);
        log.info("refreshToken = {}", newRefreshToken);

        return new TokenAuthDto(accessToken, newRefreshToken);
    }



    /**
     *
     * 회원 로그 아웃
     * 회원의 남아 있는 accessToken, refreshToken을 redis에 저장하여
     * 해당 요청은 인증이 되지 않도록 로직 구성
     *
     */
    public void logout(String accessToken, String refreshToken) {

        accessToken = removeType(accessToken);

        if (accessToken == null) {
            return ;
        }

        saveLogoutAccessToken(accessToken);
        saveLogoutRefreshToken(refreshToken);
    }

    /**
     *
     * 회원 토큰 재발급
     * 회원의 토큰 재발급 요청이 발생하면,
     * 기존에 존재하는 accessToken 재발급
     * 기존에 존재하는 accessToken remain time > refresh remain time,
     * refreshToken까지 재발급
     *
     */
    public TokenAuthDto reissue (String refreshToken) {
        isInRedisOrThrow(refreshToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String newAccessToken = tokenProviderImpl.createAccessToken(authentication);
        if (tokenProviderImpl.isMoreThanReissueTime(refreshToken))
            return TokenAuthDto.of(newAccessToken, refreshToken);

        deleteOriginRefreshToken(refreshToken);
        String newRefreshToken = createOauth2NewRefreshToken(authentication);
        return TokenAuthDto.of(newAccessToken, newRefreshToken);
    }


//    public Users getUsersFromToken(String token) {
//        String email = getEmailFromToken(token);
//
//    }


    /**
     * Oauth2
     * 새로운 refreshToken 생성
     */
    private String createOauth2NewRefreshToken(Authentication authentication) {
        String newRefreshToken = tokenProviderImpl.createRefreshToken(authentication);
        tokenRepository.saveRefreshToken(
                RefreshToken.of(newRefreshToken, getRemainingTimeFromToken(newRefreshToken)));

        return newRefreshToken;
    }

    /**
     * form
     * 새로운 refreshToken 생성
     */
    private String createFormNewRefreshToken(String email, List<Authority> authorities) {

        String refreshToken = tokenProviderImpl.createRefreshToken(email, authorities);
        long remainingTimeFromToken = tokenProviderImpl.getRemainingTimeFromToken(refreshToken);
        refreshTokenRedisRepository.save(RefreshToken.of(refreshToken, remainingTimeFromToken));

        return refreshToken;
    }


    /**
     * 새로운 refreshToken이 생성되면, 이전에 사용한 원래 refreshToken을 제거
     */
    private void deleteOriginRefreshToken(String refreshToken) {
        tokenRepository.deleteRefreshTokenById(refreshToken);
        tokenRepository.saveLogoutRefreshToken(
                LogoutRefreshToken.of(refreshToken, getRemainingTimeFromToken(refreshToken))
        );
    }

    private void isInRedisOrThrow(String refreshToken) {
        if (!tokenRepository.existRefreshTokenById(refreshToken)) {
            throw new NotExistsRefreshTokenException();
        }
    }

    /**
     * 로그 아웃시, refreshToken을 redis에 저장하여, 만료 시간 전까지,
     * refreshToken의 유효성을 저장하기 위한 메소드
     */
    private void saveLogoutRefreshToken(String refreshToken) {
        Long remainRefreshTokenTime = getRemainTime(refreshToken);
        if (remainRefreshTokenTime != null) {
            tokenRepository.saveLogoutRefreshToken(LogoutRefreshToken.of(refreshToken, remainRefreshTokenTime));
        }
    }

    /**
     * 로그 아웃시, accessToken을 redis에 저장하여, 만료 시간 전까지,
     * accessToken의 유효성을 저장하기 위한 메소드
     */
    private void saveLogoutAccessToken(String accessToken) {
        Long remainAccessTokenTime = getRemainTime(accessToken);
        if (remainAccessTokenTime != null) {
            tokenRepository.saveLogoutAccessToken(LogoutAccessToken.of(accessToken, remainAccessTokenTime));
        }
    }


    /**
     * 토큰 expiration 만료 시간 get method
     */
    private long getRemainingTimeFromToken(String token) {
        return tokenProviderImpl.getRemainingTimeFromToken(token);
    }


    /**
     * 남은 시간 체크
     */
    private Long getRemainTime(String token) { return tokenProviderImpl.getRemainTime(token);}


    /**
     * "Bearer 제거 및 에러 null 처리"
     */
    private String removeType (String token) {return tokenProviderImpl.removeType(token);}

}
