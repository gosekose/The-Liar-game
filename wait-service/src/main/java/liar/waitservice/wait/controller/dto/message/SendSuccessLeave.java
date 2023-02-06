package liar.waitservice.wait.controller.dto.message;

import liar.waitservice.wait.controller.dto.message.code.SuccessCode;
import liar.waitservice.wait.controller.dto.message.message.SuccessMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendSuccessLeave extends SendSuccess {

    private boolean leaveStatus;

    public SendSuccessLeave(String code, String message, boolean leaveStatus) {
        super(code, message);
        this.leaveStatus = leaveStatus;
    }



    public static SendSuccessLeave of(String code, String message, boolean leaveStatus) {
        return new SendSuccessLeave(code, message, leaveStatus);
    }

    public static SendSuccessLeave of(boolean leaveStatus) {
        return new SendSuccessLeave(SuccessCode.OK, SuccessMessage.OK, leaveStatus);
    }


}
