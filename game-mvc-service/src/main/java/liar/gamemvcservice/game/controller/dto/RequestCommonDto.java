package liar.gamemvcservice.game.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCommonDto {
    private String gameId;
    private String userId;
}