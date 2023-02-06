package liar.gameservice.exception.exception;

import liar.gameservice.exception.type.ExceptionCode;
import liar.gameservice.exception.type.ExceptionMessage;

public class NotEqualHostIdException extends CommonException {

    public NotEqualHostIdException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.HOST_ID_NOT_EQUAL);
    }
}
