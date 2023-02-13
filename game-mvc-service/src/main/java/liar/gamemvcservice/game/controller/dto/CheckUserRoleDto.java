package liar.gamemvcservice.game.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckUserRoleDto {
    private String gameId;
    private String userId;
}
