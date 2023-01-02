package liar.game.common.exception;

import org.springframework.http.HttpStatus;

public abstract class AuthException extends ApplicationException {

    public AuthException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

    public AuthException(String errorCode, HttpStatus httpStatus, String message, Throwable cause) {
        super(errorCode, httpStatus, message, cause);
    }
}
