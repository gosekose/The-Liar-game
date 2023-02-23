package liar.resultservice.exception.exception;

import liar.resultservice.exception.type.ExceptionCode;
import liar.resultservice.exception.type.ExceptionMessage;

public class NotFoundGameResultException extends CommonException {

    public NotFoundGameResultException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.NOT_FOUND);
    }
}
