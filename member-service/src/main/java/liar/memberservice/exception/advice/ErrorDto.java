package liar.memberservice.exception.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorDto {

    private String code;
    private String message;
}
