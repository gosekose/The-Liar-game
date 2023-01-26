package liar.gateway.domain;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "logoutRefreshToken")
public class LogoutRefreshToken extends Token {

    public LogoutRefreshToken(String id, long expiration) {
        super(id, expiration);
    }

    public static  LogoutRefreshToken of (String refreshToken, long expiration) {
        return new LogoutRefreshToken(refreshToken, expiration);
    }
}
