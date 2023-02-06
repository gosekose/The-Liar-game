package liar.gameservice.exception.exception;

import liar.gameservice.exception.type.ExceptionCode;
import liar.gameservice.exception.type.ExceptionMessage;

public class NotFoundWaitRoomException extends CommonException {
    public NotFoundWaitRoomException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.WAITROOM_NOT_FOUND);
    }
}
