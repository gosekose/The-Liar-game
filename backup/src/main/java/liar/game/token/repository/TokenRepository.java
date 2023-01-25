package liar.game.token.repository;

import liar.game.token.domain.LogoutAccessToken;
import liar.game.token.domain.LogoutRefreshToken;
import liar.game.token.domain.RefreshToken;

public interface TokenRepository {

    void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken);

    void saveLogoutRefreshToken(LogoutRefreshToken logoutRefreshToken);

    void saveRefreshToken(RefreshToken refreshToken);

    boolean existsLogoutAccessTokenById(String token);

    boolean existLogoutRefreshTokenById(String token);

    boolean existRefreshTokenById(String token);

    void deleteRefreshTokenById(String token);

}
