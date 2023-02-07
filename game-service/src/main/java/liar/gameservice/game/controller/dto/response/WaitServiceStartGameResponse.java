package liar.gameservice.game.controller.dto.response;

import liar.gameservice.game.controller.dto.message.SuccessCode;
import liar.gameservice.game.controller.dto.message.SuccessResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitServiceStartGameResponse extends CustomResponse {
    private boolean status;

    public WaitServiceStartGameResponse(String code, String message, boolean status) {
        super(code, message);
        this.status = status;
    }

    public WaitServiceStartGameResponse(boolean status) {
        this.status = status;
    }

    public static WaitServiceStartGameResponse of(boolean status) {
        return new WaitServiceStartGameResponse(SuccessCode.OK, SuccessResponse.OK, status);
    }
}
