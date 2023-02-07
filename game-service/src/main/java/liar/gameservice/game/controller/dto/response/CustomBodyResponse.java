package liar.gameservice.game.controller.dto.response;

import liar.gameservice.game.controller.dto.message.SuccessCode;
import liar.gameservice.game.controller.dto.message.SuccessResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomBodyResponse<T> extends CustomResponse {

    public T body;

    public CustomBodyResponse(String code, String message, T body) {
        super(code, message);
        this.body = body;
    }

    public static <T> CustomBodyResponse of(String code, String message, T body) {
        return new CustomBodyResponse(code, message, body);
    }

    public static <T> CustomBodyResponse of(T body) {
        return new CustomBodyResponse(SuccessCode.OK, SuccessResponse.OK, body);
    }

}
