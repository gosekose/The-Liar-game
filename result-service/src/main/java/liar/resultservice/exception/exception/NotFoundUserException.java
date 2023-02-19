package liar.resultservice.exception.exception;


import liar.resultservice.exception.type.ExceptionCode;
import liar.resultservice.exception.type.ExceptionMessage;

public class NotFoundUserException extends CommonException {

    public NotFoundUserException() {super(ExceptionCode.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);}
}
