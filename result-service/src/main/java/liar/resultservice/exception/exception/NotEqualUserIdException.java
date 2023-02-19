package liar.resultservice.exception.exception;

import liar.resultservice.exception.type.ExceptionCode;
import liar.resultservice.exception.type.ExceptionMessage;

public class NotEqualUserIdException extends CommonException {

    public NotEqualUserIdException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.NOT_FOUND);
    }
}
