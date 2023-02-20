package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.controller.dto.request.VoteLiarRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VoteLiarDto {
    private String gameId;
    private String userId;
    private String liarId;

    public static VoteLiarDto of(VoteLiarRequest request) {
        return new VoteLiarDto(request.getGameId(), request.getUserId(), request.getLiarId());
    }

}
