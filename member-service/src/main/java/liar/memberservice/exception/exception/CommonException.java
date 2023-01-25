package liar.memberservice.exception.exception;

public abstract class CommonException extends ApplicationException {

    public CommonException(String errorCode, String message) {
        super(errorCode, message);
    }

    public CommonException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
