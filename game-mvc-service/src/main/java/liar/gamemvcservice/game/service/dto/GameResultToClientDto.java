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
public class GameResultToClientDto {
    private String gameId;
    private GameRole winner;
    private List<PlayersInfoDto> playersInfo;

    public static GameResultToClientDto of(String gameId, GameRole whoWin, List<PlayersInfoDto> playersInfoDto) {
        return new GameResultToClientDto(gameId, whoWin, playersInfoDto);
    }
}
