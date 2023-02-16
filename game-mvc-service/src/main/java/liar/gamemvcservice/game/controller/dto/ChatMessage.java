package liar.gamemvcservice.game.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @NotNull
    private String userId;

    @NotNull
    private String charMessage;
}
