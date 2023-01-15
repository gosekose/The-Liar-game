package liar.game.common.exception.advice;

import liar.game.common.exception.exception.BindingInvalidException;
import liar.game.common.exception.exception.NotExistsRefreshTokenException;
import liar.game.common.exception.exception.NotFoundUserException;
import liar.game.common.exception.exception.UserRegisterConflictException;
import liar.game.common.exception.type.ExceptionCode;
import liar.game.common.exception.type.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class LoginControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<ErrorDto> bindingInvalidHandler(BindingInvalidException e) {
        return new ResponseEntity<>(new ErrorDto(e.getErrorCode(), e.getMessage()), BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> illegalHandler(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorDto("BAD", e.getMessage()), BAD_REQUEST);
    }
    
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler
    public ResponseEntity<ErrorDto> notExistsRefreshTokenHandler(NotExistsRefreshTokenException e) {
        return new ResponseEntity<>(new ErrorDto(e.getErrorCode(), e.getMessage()), UNAUTHORIZED);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler
    public ResponseEntity<ErrorDto> userNotFoundHandler(NotFoundUserException e) {
        return new ResponseEntity<>(new ErrorDto(e.getErrorCode(), e.getMessage()), NOT_FOUND);
    }
    
    @ResponseStatus(CONFLICT)
    @ExceptionHandler
    public ResponseEntity<ErrorDto> userEmailConflict(UserRegisterConflictException e) {
        return new ResponseEntity<>(new ErrorDto(e.getErrorCode(), e.getMessage()), CONFLICT);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<ErrorDto> internalServerErrorHandler(Exception e) {

        return new ResponseEntity<>(new ErrorDto(ExceptionCode.INTERNAL_SERVER_ERROR, ExceptionMessage.INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);
    }

}
