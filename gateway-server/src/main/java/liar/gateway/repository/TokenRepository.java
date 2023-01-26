package liar.gateway.repository;


import liar.gateway.domain.LogoutAccessToken;
import liar.gateway.domain.LogoutRefreshToken;
import liar.gateway.domain.RefreshToken;

public interface TokenRepository {

    void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken);

    void saveLogoutRefreshToken(LogoutRefreshToken logoutRefreshToken);

    void saveRefreshToken(RefreshToken refreshToken);

    boolean existsLogoutAccessTokenById(String token);

    boolean existLogoutRefreshTokenById(String token);

    boolean existRefreshTokenById(String token);

    void deleteRefreshTokenById(String token);

}
