package liar.waitservice.exception.exception;

import liar.waitservice.exception.type.ExceptionCode;
import liar.waitservice.exception.type.ExceptionMessage;

public class NotEqualHostIdException extends CommonException {

    public NotEqualHostIdException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.HOST_ID_NOT_EQUAL);
    }
}
