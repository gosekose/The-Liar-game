package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.GameRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameResultBaseDto {
    private String gameId;
    private GameRole winner;
    private List<PlayerResultInfoDto> playersInfo;

    public static GameResultBaseDto of(String gameId, GameRole winner, List<PlayerResultInfoDto> playersInfo) {
        return new GameResultBaseDto(gameId, winner, playersInfo);
    }
}
