package liar.gamemvcservice.game.domain;

import jakarta.persistence.Id;
import liar.gamemvcservice.exception.exception.GameTurnEndException;
import liar.gamemvcservice.exception.exception.NotUserTurnException;
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

    private List<String> playerTurnsConsistingOfUserId;
    private int nowTurn;

    public GameTurn(String gameId, List<String> playerTurnsConsistingOfUserId) {
        this.id = UUID.randomUUID().toString();
        this.gameId = gameId;
        this.playerTurnsConsistingOfUserId = playerTurnsConsistingOfUserId;
        this.nowTurn = 0;
    }

    /**
     * 사용자의 턴이 맞다면, nowTurn++하고, gameTurn을 출력한다.
     * 맞지 않다면, NotUserTurnException을 호출한다.
     */
    public GameTurn updateTurnCntWhenPlayerTurnIsValidated(String requestId) {
        if (isPlayerTurn(requestId)) {
           this.nowTurn++;
           return this;
        }
        throw new NotUserTurnException();
    }

    /**
     * 사용자가 시간안에 요청을 하지않으면, 타임아웃이 실행된다.
     */
    public GameTurn updateTurnCntByTimeOut() {
        this.nowTurn++;
        return this;
    }

    /**
     * 플레이어의 턴이 맞는지 확인하는 메소드
     */
    private boolean isPlayerTurn(String requestId) {
        int idx = nowTurn % playerTurnsConsistingOfUserId.size();

        if (playerTurnsConsistingOfUserId.get(idx).equals(requestId)) {
            return true;
        }
        return false;
    }

    /**
     * 플레이어의 다음 턴을 알리는 매소드 (기본 로테이션 횟수 2회)
     * 현재 플레이어가 게임의 마지막 턴인 경우, next 턴은 없다
     */
    public NextTurn setIfExistsNextTurn() {
        int lastTurn = playerTurnsConsistingOfUserId.size() * 2;

        if (nowTurn > lastTurn) throw new GameTurnEndException();
        else if (nowTurn == lastTurn) return new NextTurn(null, true);
        else return new NextTurn(playerTurnsConsistingOfUserId
                    .get(nowTurn % playerTurnsConsistingOfUserId.size()), false);
    }
}

