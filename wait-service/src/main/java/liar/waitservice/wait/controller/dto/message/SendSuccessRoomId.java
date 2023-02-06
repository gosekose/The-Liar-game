package liar.waitservice.wait.controller.dto.message;

import liar.waitservice.wait.controller.dto.message.code.SuccessCode;
import liar.waitservice.wait.controller.dto.message.message.SuccessMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendSuccessRoomId<T> extends SendSuccess {

    public T waitRoomId;

    public SendSuccessRoomId(String code, String message, T waitRoomId) {
        super(code, message);
        this.waitRoomId = waitRoomId;
    }

    public static <T> SendSuccessRoomId of(String code, String message, T waitRoomId) {
        return new SendSuccessRoomId(code, message, waitRoomId);
    }

    public static <T> SendSuccessRoomId of(T waitRoomId) {
        return new SendSuccessRoomId(SuccessCode.OK, SuccessMessage.OK, waitRoomId);
    }

}
