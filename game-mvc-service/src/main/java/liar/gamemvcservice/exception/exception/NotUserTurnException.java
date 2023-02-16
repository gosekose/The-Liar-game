package liar.gamemvcservice.exception.exception;


import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class NotUserTurnException extends CommonException {

    public NotUserTurnException() {super(ExceptionCode.BAD_REQUEST, ExceptionMessage.NOT_USER_TURN);}
}
