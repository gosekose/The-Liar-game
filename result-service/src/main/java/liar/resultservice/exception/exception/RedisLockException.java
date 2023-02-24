package liar.resultservice.exception.exception;

import liar.resultservice.exception.type.ExceptionCode;
import liar.resultservice.exception.type.ExceptionMessage;

public class RedisLockException extends CommonException {

    public RedisLockException() {
        super(ExceptionCode.CONFLICT, ExceptionMessage.REDIS_ROCK_EXCEPTION);
    }
}
