package liar.gameservice.game.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RedisHash(value = "Game")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game extends BaseRedisTemplateEntity<String> {

    @Id
    private String id;

    private String roomId;
    private String hostId;
    private String name;
    private Topic topic;
    private List<String> playerIds = new ArrayList<>();

    @Builder
    public Game(String id, String roomId, String hostId, String name, Topic topic) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.hostId = hostId;
        this.name = name;
        this.topic = topic;
    }

    public Game(String id, Topic topic) {
        this.id = UUID.randomUUID().toString();
        this.topic = topic;
    }

    public void addPlayer(String playerId) {
        playerIds.add(playerId);
    }

}
