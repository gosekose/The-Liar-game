package liar.gamemvcservice.game.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Index;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
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
    private Topic topic;

    protected Game (SetUpGameDto setUpGameDto) {
        this.id = UUID.randomUUID().toString();
        this.roomId = setUpGameDto.getRoomId();
        this.hostId = setUpGameDto.getHostId();
        this.gameName = setUpGameDto.getRoomName();
        this.playerIds = setUpGameDto.getUserIds();
    }

    public static Game of(SetUpGameDto setUpGameDto) {
        return new Game(setUpGameDto);
    }

    public Game updateTopicOfGame(Topic topic) {
        this.topic = topic;
        return this;
    }

    public List<String> shufflePlayer() {
        Collections.shuffle(playerIds);
        return this.getPlayerIds();
    }
}
