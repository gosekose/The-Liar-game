package liar.gameservice.game.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "JoinMember")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinMember {

    @Id @GeneratedValue
    private Long id;

    private String roomId;
    private Player player;
    private String topic;

    static class Player {
        private String userId;
        private GameRole gameRole;
    }

    public JoinMember(String roomId, Player player, String topic) {
        this.roomId = roomId;
        this.player = player;
        this.topic = topic;
    }
}
