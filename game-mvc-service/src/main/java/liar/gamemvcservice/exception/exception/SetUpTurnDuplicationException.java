package liar.gamemvcservice.exception.exception;

import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class SetUpTurnDuplicationException extends CommonException {
    public SetUpTurnDuplicationException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BADREQUEST);
    }
}
