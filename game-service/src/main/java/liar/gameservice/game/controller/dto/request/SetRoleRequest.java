package liar.gameservice.game.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetRoleRequest {

    private String hostId;
    private String roomId;

}
