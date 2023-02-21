package liar.gamemvcservice.exception.exception;


import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class NotFoundVoteException extends CommonException {

    public NotFoundVoteException() {super(ExceptionCode.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);}
}
