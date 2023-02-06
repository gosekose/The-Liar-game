package liar.gameservice.exception.advice;

import liar.gameservice.exception.exception.BindingInvalidException;
import liar.gameservice.exception.exception.NotExistsRefreshTokenException;
import liar.gameservice.exception.exception.NotFoundUserException;
import liar.gameservice.exception.type.ExceptionCode;
import liar.gameservice.exception.type.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionControllerAdvice {

    /**
     *  바인딩 에러
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    public Mono<ResponseEntity<ErrorDto>> bindingInvalidHandler(BindingInvalidException e) {
        return Mono.just(new ResponseEntity<>(new ErrorDto(e.getErrorCode(), e.getMessage()), BAD_REQUEST));
    }


    /**
     *  권한 없는 사용자 접근
     */
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler
    public Mono<ResponseEntity<ErrorDto>> notExistsTokenHandler(NotExistsRefreshTokenException e) {
        return Mono.just(new ResponseEntity<>(new ErrorDto(e.getErrorCode(), e.getMessage()), UNAUTHORIZED));
    }


    /**
     *  등록되지 않은 유저 접근
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler
    public Mono<ResponseEntity<ErrorDto>> userNotFoundHandler(NotFoundUserException e) {
        return Mono.just(new ResponseEntity<>(new ErrorDto(e.getErrorCode(), e.getMessage()), NOT_FOUND));
    }


    /**
     *  공통 4XX 에러
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorDto>> illegalHandler(IllegalArgumentException e) {
        return Mono.just(new ResponseEntity<>(new ErrorDto(ExceptionCode.BAD_REQUEST, ExceptionMessage.BADREQUEST), BAD_REQUEST));
    }


    /**
     *  공통 5XX 에러
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public Mono<ResponseEntity<ErrorDto>> internalServerErrorHandler(Exception e) {

        return Mono.just(new ResponseEntity<>(new ErrorDto(ExceptionCode.INTERNAL_SERVER_ERROR, ExceptionMessage.INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR));
    }

}
