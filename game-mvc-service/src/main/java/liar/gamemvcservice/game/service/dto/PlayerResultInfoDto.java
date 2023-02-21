package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.GameRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResultInfoDto {

    private String userId;
    private GameRole gameRole;
    private Boolean answers;

}
