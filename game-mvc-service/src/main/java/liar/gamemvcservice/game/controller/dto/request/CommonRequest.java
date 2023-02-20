package liar.gamemvcservice.game.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequest {
    private String gameId;
    private String userId;
}
