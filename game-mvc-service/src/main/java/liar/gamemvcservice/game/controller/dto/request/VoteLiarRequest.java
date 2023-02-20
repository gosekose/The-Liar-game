package liar.gamemvcservice.game.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
public class VoteLiarRequest {

    @NotNull
    private String gameId;
    @NotNull
    private String userId;
    @NotNull
    private String liarId;

}
