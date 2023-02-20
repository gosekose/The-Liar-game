package liar.gamemvcservice.game.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetUpGameRequest {
    private String roomId;
    private String hostId;
    private String roomName;
    private List<String> userIds;
}