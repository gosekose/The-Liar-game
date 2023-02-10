package liar.gameservice.exception.exception;

import liar.gameservice.exception.type.ExceptionCode;
import liar.gameservice.exception.type.ExceptionMessage;

public class EntityNotFoundException extends CommonException {
    public EntityNotFoundException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.NOT_FOUND_ENTITY);
    }
}
