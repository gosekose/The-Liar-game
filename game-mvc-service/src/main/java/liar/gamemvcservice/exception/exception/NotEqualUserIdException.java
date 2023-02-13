package liar.gamemvcservice.exception.exception;

import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class NotEqualUserIdException extends CommonException {

    public NotEqualUserIdException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.HEADER_USERID_NOT_EQUAL_REQUEST_ID);
    }
}
