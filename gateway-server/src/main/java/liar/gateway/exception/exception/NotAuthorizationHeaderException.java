package liar.gateway.exception.exception;

import liar.gateway.exception.type.ExceptionCode;
import liar.gateway.exception.type.ExceptionMessage;

public class NotAuthorizationHeaderException extends CommonException {

    public NotAuthorizationHeaderException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.USER_NOT_AUTHORIZATION_HEADER);
    }
}
