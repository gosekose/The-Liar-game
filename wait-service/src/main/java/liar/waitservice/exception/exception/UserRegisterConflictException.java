package liar.waitservice.exception.exception;

import liar.waitservice.exception.type.ExceptionCode;
import liar.waitservice.exception.type.ExceptionMessage;

public class UserRegisterConflictException extends CommonException {
    public UserRegisterConflictException() {
        super(ExceptionCode.CONFLICT, ExceptionMessage.USER_REGISTER_CONFLICT);
    }
}
