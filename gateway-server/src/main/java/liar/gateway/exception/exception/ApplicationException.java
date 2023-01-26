package liar.gateway.exception.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ApplicationException extends RuntimeException {

    private final String errorCode;

    protected ApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
