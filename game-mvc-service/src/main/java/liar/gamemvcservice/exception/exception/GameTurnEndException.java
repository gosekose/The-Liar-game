package liar.gamemvcservice.exception.exception;

import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class GameTurnEndException extends CommonException {
    public GameTurnEndException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BADREQUEST);
    }
}
