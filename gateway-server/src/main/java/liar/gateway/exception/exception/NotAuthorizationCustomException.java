package liar.gateway.exception.exception;

import liar.gateway.exception.type.ExceptionCode;
import liar.gateway.exception.type.ExceptionMessage;

public class NotAuthorizationCustomException extends CommonException {

    public NotAuthorizationCustomException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.USER_NOT_AUTHORIZATION_HEADER);
    }
}
