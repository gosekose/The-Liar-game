package liar.gateway.exception.exception;


import liar.gateway.exception.type.ExceptionCode;
import liar.gateway.exception.type.ExceptionMessage;

public class NotFoundUserException extends CommonException {

    public NotFoundUserException() {super(ExceptionCode.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);}
}
