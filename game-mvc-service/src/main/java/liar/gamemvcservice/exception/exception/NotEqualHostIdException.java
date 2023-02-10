package liar.gamemvcservice.exception.exception;

import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class NotEqualHostIdException extends CommonException {

    public NotEqualHostIdException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.HOST_ID_NOT_EQUAL);
    }
}
