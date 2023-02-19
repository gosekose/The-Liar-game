package liar.resultservice.exception.exception;

import liar.resultservice.exception.type.ExceptionCode;
import liar.resultservice.exception.type.ExceptionMessage;

public class BindingInvalidException extends CommonException {
    public BindingInvalidException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BINDING_INVALID);
    }
}
