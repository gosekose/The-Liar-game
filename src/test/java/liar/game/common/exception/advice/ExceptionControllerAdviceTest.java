package liar.game.common.exception.advice;

import com.google.gson.Gson;
import liar.game.common.exception.exception.BindingInvalidException;
import liar.game.member.service.dto.FormRegisterUserDto;
import liar.game.token.controller.controller.LoginController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ExceptionControllerAdviceTest {

    @Autowired
    ExceptionControllerAdvice controllerAdvice;

    @Test
    @DisplayName("바인딩 에러 발생하면 400을 호출한다.")
    public void doBindingError() throws Exception {
        //given

        BindingInvalidException e = new BindingInvalidException();

        //when
        ResponseEntity<ErrorDto> responseEntity = controllerAdvice.bindingInvalidHandler(e);

        //then
        assertThat(responseEntity.getBody().getCode()).isEqualTo("400");
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("유효하지 않은 요청입니다.");
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }



}