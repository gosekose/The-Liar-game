package liar.gamemvcservice.exception.exception;

import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class BindingInvalidException extends CommonException {
    public BindingInvalidException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BINDING_INVALID);
    }
}
