package liar.gamemvcservice.exception.exception;

import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class NotFoundGameException extends CommonException {

    public NotFoundGameException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.NOT_FOUND);
    }
}
