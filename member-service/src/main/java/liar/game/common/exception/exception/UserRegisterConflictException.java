package liar.game.common.exception.exception;

import liar.game.common.exception.type.ExceptionCode;
import liar.game.common.exception.type.ExceptionMessage;

public class UserRegisterConflictException extends CommonException {
    public UserRegisterConflictException() {
        super(ExceptionCode.CONFLICT, ExceptionMessage.USER_REGISTER_CONFLICT);
    }
}
