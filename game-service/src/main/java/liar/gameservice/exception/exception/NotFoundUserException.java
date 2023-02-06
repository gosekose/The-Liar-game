package liar.gameservice.exception.exception;


import liar.gameservice.exception.type.ExceptionCode;
import liar.gameservice.exception.type.ExceptionMessage;

public class NotFoundUserException extends CommonException {

    public NotFoundUserException() {super(ExceptionCode.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);}
}
