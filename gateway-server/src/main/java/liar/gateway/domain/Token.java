package liar.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Token {

    @Id
    private String id;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

}
