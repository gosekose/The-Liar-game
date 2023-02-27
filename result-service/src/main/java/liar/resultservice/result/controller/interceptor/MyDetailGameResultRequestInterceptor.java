package liar.resultservice.result.controller.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import liar.resultservice.exception.exception.BadRequestException;
import liar.resultservice.exception.exception.CommonException;
import liar.resultservice.exception.exception.NotEqualUserIdException;
import liar.resultservice.result.controller.dto.request.MyDetailGameResultRequest;
import liar.resultservice.result.controller.interceptor.anno.MyDetailGameResult;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MyDetailGameResultRequestInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            MyDetailGameResult annotation = handlerMethod.getMethodAnnotation(MyDetailGameResult.class);

            if (annotation != null) {
                String userId = request.getPathInfo().split("/")[1];
                String headerUserId = request.getHeader("userId");
                MyDetailGameResultRequest myDetailGameResultRequest = objectMapper.readValue(request.getReader(), MyDetailGameResultRequest.class);

                if (!(headerUserId.equals(userId) && myDetailGameResultRequest.getUserId().equals(userId))) {
                    throw new NotEqualUserIdException();
                }

                // TO DO
                if (!myDetailGameResultRequest.hasAtMostOneNonUserIdField()) {
                    throw new BadRequestException();
                }
            }
        }

        return true;
    }
}