package liar.waitservice.wait.controller.dto.message;

import liar.waitservice.wait.controller.dto.message.code.SuccessCode;
import liar.waitservice.wait.controller.dto.message.message.SuccessMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendSuccessProcess extends SendSuccess {
    private boolean status;

    public SendSuccessProcess(String code, String message, boolean status) {
        super(code, message);
        this.status = status;
    }

    public SendSuccessProcess(boolean status) {
        this.status = status;
    }

    public static SendSuccessProcess of(boolean status) {
        return new SendSuccessProcess(SuccessCode.OK, SuccessMessage.OK, status);
    }
}
