package liar.gamemvcservice.exception.exception;


import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class NotFoundUserException extends CommonException {

    public NotFoundUserException() {super(ExceptionCode.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);}
}
