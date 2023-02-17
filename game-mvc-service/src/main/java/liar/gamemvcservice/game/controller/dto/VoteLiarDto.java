package liar.gamemvcservice.game.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteLiarDto {

    private String gameId;
    private String userId;
    private String liarId;

}
