package liar.gamemvcservice.game.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Index;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@RedisHash("Game")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {

    @Id
    private String id;
    private String gameName;
    private String roomId;
    private String hostId;
    private List<String> playerIds;

    public void addPlayers(String playerId) {
        playerIds.add(playerId);
    }
}
