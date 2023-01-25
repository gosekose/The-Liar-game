package liar.game.common.exception.exception;


import liar.game.common.exception.type.ExceptionCode;
import liar.game.common.exception.type.ExceptionMessage;

public class NotFoundUserException extends CommonException {

    public NotFoundUserException() {super(ExceptionCode.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);}
}
