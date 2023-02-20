package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.controller.dto.request.SetUpGameRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetUpGameDto {
    private String roomId;
    private String hostId;
    private String roomName;
    private List<String> userIds;

    public static SetUpGameDto of(SetUpGameRequest request) {
        return new SetUpGameDto(request.getRoomId(), request.getHostId(),
                request.getRoomName(), request.getUserIds());
    }
}
