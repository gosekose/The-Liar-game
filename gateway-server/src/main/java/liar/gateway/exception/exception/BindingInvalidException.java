package liar.gateway.exception.exception;


import liar.gateway.exception.type.ExceptionCode;
import liar.gateway.exception.type.ExceptionMessage;

public class BindingInvalidException extends CommonException {
    public BindingInvalidException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BINDING_INVALID);
    }
}
