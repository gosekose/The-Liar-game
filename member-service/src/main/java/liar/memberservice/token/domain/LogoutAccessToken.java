package liar.memberservice.token.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "logoutAccessToken")
@NoArgsConstructor
public class LogoutAccessToken extends Token{

    public LogoutAccessToken(String id, long expiration) {
        super(id, expiration);
    }

    public static LogoutAccessToken of (String accessToken, long expiration) {
        return new LogoutAccessToken(accessToken, expiration);
    }

}
