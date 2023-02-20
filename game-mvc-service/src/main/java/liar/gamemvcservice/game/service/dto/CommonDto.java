package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.controller.dto.request.CommonRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonDto {
    private String gameId;
    private String userId;

    public static CommonDto of(CommonRequest request) {
        return new CommonDto(request.getGameId(), request.getUserId());
    }
}
