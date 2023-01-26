package liar.gateway.exception.exception;

import liar.gateway.exception.type.ExceptionCode;
import liar.gateway.exception.type.ExceptionMessage;

public class NotExistsRefreshTokenException extends CommonException {

    public NotExistsRefreshTokenException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.USER_NOT_REFRESHTOKEN);
    }
}
