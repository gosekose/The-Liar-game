package liar.gamemvcservice.exception.exception;

import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class NotFoundWaitRoomException extends CommonException {
    public NotFoundWaitRoomException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.WAITROOM_NOT_FOUND);
    }
}
