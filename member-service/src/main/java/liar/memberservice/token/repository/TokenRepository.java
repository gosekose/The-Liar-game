package liar.memberservice.token.repository;

import liar.memberservice.token.domain.LogoutAccessToken;
import liar.memberservice.token.domain.LogoutRefreshToken;
import liar.memberservice.token.domain.RefreshToken;

public interface TokenRepository {

    void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken);

    void saveLogoutRefreshToken(LogoutRefreshToken logoutRefreshToken);

    void saveRefreshToken(RefreshToken refreshToken);

    boolean existsLogoutAccessTokenById(String token);

    boolean existLogoutRefreshTokenById(String token);

    boolean existRefreshTokenById(String token);

    void deleteRefreshTokenById(String token);

}
