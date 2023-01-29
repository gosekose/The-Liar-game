package liar.waitservice.exception.exception;

import liar.waitservice.exception.type.ExceptionCode;
import liar.waitservice.exception.type.ExceptionMessage;

public class NotExistsRoomIdException extends CommonException {
    public NotExistsRoomIdException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.WAITROOM_NOT_FOUND);
    }
}
