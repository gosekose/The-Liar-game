package liar.waitservice.wait.controller.dto.message;

import liar.waitservice.wait.controller.dto.message.code.SuccessCode;
import liar.waitservice.wait.controller.dto.message.message.SuccessMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendSuccessDelete extends SendSuccess {

    private boolean deleteStatus;

    public SendSuccessDelete(String code, String message, boolean deleteStatus) {
        super(code, message);
        this.deleteStatus = deleteStatus;
    }

    public static SendSuccessDelete of(String code, String message, boolean deleteStatus) {
        return new SendSuccessDelete(code, message, deleteStatus);
    }

    public static SendSuccessDelete of(boolean deleteStatus) {
        return new SendSuccessDelete(SuccessCode.OK, SuccessMessage.OK, deleteStatus);
    }
}
