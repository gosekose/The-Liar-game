package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.GameRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameResultToClient {
    private String gameId;
    private GameRole winner;
    private List<PlayersInfoDto> playersInfo;

    public static GameResultToClient of(String gameId, GameRole whoWin, List<PlayersInfoDto> playersInfoDto) {
        return new GameResultToClient(gameId, whoWin, playersInfoDto);
    }
}
