package liar.gamemvcservice.game.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Index;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@RedisHash("JoinPlayer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinPlayer {

    @Id
    private String id;

    @Indexed
    private String gameId;
    private Player player;

    public JoinPlayer(String gameId, Player player) {
        this.id = gameId + "_" + player.getUserId();
        this.gameId = gameId;
        this.player = player;
    }
}
