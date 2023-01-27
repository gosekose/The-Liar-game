package liar.gateway.exception.exception;

import liar.gateway.exception.exception.CommonException;
import liar.gateway.exception.type.ExceptionCode;
import liar.gateway.exception.type.ExceptionMessage;

public class NotUserIdHeaderException extends CommonException {
    public NotUserIdHeaderException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.USER_NOT_USERID_HEADER);
    }
}
