package liar.game.common.exception;

import org.springframework.http.HttpStatus;

public class NotExistsRefreshTokenException extends AuthException {

    private static final String MESSAGE = "존재하지 않는 refreshToken 입니다,";
    private static final String CODE = "401";

    public NotExistsRefreshTokenException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
