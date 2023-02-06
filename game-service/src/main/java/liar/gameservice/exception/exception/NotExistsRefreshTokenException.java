package liar.gameservice.exception.exception;

import liar.gameservice.exception.type.ExceptionCode;
import liar.gameservice.exception.type.ExceptionMessage;

public class NotExistsRefreshTokenException extends CommonException {

    public NotExistsRefreshTokenException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.USER_NOT_REFRESHTOKEN);
    }
}
