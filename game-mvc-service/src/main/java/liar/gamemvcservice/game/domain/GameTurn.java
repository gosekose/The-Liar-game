package liar.gamemvcservice.game.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;
import java.util.UUID;

@Getter
@RedisHash("GameTurn")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameTurn {

    @Id
    private String id;

    @Indexed
    private String gameId;

    private List<String> playerTurn;
    private int nowTurn;

    public GameTurn(String gameId, List<String> playerTurn) {
        this.id = UUID.randomUUID().toString();
        this.gameId = gameId;
        this.playerTurn = playerTurn;
        this.nowTurn = 0;
    }

    public boolean isPlayerTurn(String requestId) {
        int idx = nowTurn % playerTurn.size();

        if (playerTurn.get(idx).equals(requestId)) {
            return true;
        }
        return false;
    }

    public GameTurn updateTurnCnt(String requestId) {
        if (isPlayerTurn(requestId)) {
           this.nowTurn++;
           return this;
        }
        return this;
    }
}

