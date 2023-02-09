package liar.gameservice.game.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoRequest {
    private String roomId;
    private String hostId;
    private String name;
    private List<String> players;
}
