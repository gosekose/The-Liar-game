package liar.game.common.exception.exception;

import liar.game.common.exception.type.ExceptionCode;
import liar.game.common.exception.type.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class NotExistsRefreshTokenException extends CommonException {

    public NotExistsRefreshTokenException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.USER_NOT_REFRESHTOKEN);
    }
}
