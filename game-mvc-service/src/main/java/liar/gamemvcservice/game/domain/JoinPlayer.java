package liar.gamemvcservice.game.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("JoinPlayer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinPlayer {

    @Id
    private String id;

    private

}
