package liar.gameservice.game.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@RedisHash(value = "JoinMember")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinMember {

    @Id
    private String id;

    private String roomId;
    private Player player;
    private String topic;

    static class Player {
        private String userId;
        private GameRole gameRole;
    }

    public JoinMember(String roomId, Player player, String topic) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.player = player;
        this.topic = topic;
    }
}
