package liar.gateway.exception.exception;

import liar.gateway.exception.type.ExceptionCode;
import liar.gateway.exception.type.ExceptionMessage;

public class UserRegisterConflictException extends CommonException {
    public UserRegisterConflictException() {
        super(ExceptionCode.CONFLICT, ExceptionMessage.USER_REGISTER_CONFLICT);
    }
}
