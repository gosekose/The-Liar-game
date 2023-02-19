package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.GameRole;
import liar.gamemvcservice.game.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameResultMessage {
    private AtomicLong id;
    private String gameId;
    private GameRole whoWin;
    private ConcurrentHashMap<Player, Boolean> playersInfoAndWhoRightAnswers;
}
