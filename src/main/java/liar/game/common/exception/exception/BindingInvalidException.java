package liar.game.common.exception.exception;

import liar.game.common.exception.type.ExceptionCode;
import liar.game.common.exception.type.ExceptionMessage;

public class BindingInvalidException extends CommonException {
    public BindingInvalidException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BINDING_INVALID);
    }
}
