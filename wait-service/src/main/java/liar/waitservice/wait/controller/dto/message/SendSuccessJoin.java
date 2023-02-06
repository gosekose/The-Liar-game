package liar.waitservice.wait.controller.dto.message;

import liar.waitservice.wait.controller.dto.message.code.SuccessCode;
import liar.waitservice.wait.controller.dto.message.message.SuccessMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendSuccessJoin extends SendSuccess {

    private boolean joinStatus;

    public SendSuccessJoin(String code, String message, boolean joinStatus) {
        super(code, message);
        this.joinStatus = joinStatus;
    }

    public static SendSuccessJoin of(String code, String message, boolean joinStatus) {
        return new SendSuccessJoin(code, message, joinStatus);
    }

    public static SendSuccessJoin of(boolean joinStatus) {
        return new SendSuccessJoin(SuccessCode.OK, SuccessMessage.OK, joinStatus);
    }
}
