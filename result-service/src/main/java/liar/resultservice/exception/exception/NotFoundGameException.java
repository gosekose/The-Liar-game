package liar.resultservice.exception.exception;

import liar.resultservice.exception.type.ExceptionCode;
import liar.resultservice.exception.type.ExceptionMessage;

public class NotFoundGameException extends CommonException {

    public NotFoundGameException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.NOT_FOUND);
    }
}
