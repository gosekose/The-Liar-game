package liar.gamemvcservice.game.domain;

import jakarta.persistence.Id;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@RedisHash("Game")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {

    @Id
    private String id;
    private String roomId;
    private String hostId;
    private String gameName;
    private List<String> playerIds;
    private String liarId;
    private Topic topic;
    private boolean sendMessage;
    private boolean sendMessageSuccess;

    protected Game (SetUpGameDto setUpGameDto) {
        this.id = UUID.randomUUID().toString();
        this.roomId = setUpGameDto.getRoomId();
        this.hostId = setUpGameDto.getHostId();
        this.gameName = setUpGameDto.getRoomName();
        this.playerIds = setUpGameDto.getUserIds();
        this.sendMessage = false;
        this.sendMessageSuccess = false;
    }

    public static Game of(SetUpGameDto setUpGameDto) {
        return new Game(setUpGameDto);
    }

    public Game updateTopicOfGame(Topic topic, String liarId) {
        this.topic = topic;
        this.liarId = liarId;
        return this;
    }

    public List<String> shufflePlayer() {
        Collections.shuffle(playerIds);
        return this.getPlayerIds();
    }

    public Game sendMessage() {
        this.sendMessage = true;
        return this;
    }

    public Game sendMessageSuccess() {
        this.sendMessageSuccess = true;
        return this;
    }

    @Override
    public String toString() {
        return "Game:" + id;
    }
}
