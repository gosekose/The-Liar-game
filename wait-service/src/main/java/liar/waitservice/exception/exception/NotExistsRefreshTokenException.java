package liar.waitservice.exception.exception;

import liar.waitservice.exception.type.ExceptionCode;
import liar.waitservice.exception.type.ExceptionMessage;

public class NotExistsRefreshTokenException extends CommonException {

    public NotExistsRefreshTokenException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.USER_NOT_REFRESHTOKEN);
    }
}
