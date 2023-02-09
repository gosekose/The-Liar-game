package liar.gameservice.game.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@RedisHash(value = "JoinPlayer")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinPlayer {

    @Id
    private String id;

    private String roomId;
    private Player player;

    public JoinPlayer(String roomId, Player player) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.player = player;
    }
}
