package liar.gamemvcservice.game.controller.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import liar.gamemvcservice.exception.exception.NotEqualUserIdException;
import liar.gamemvcservice.game.service.dto.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getPathInfo().split("/")[1];

        String headerUserId = request.getHeader("userId");
        CommonDto dto = objectMapper
                .readValue(request.getReader().lines().collect(Collectors.joining()),
                        CommonDto.class);

        if (!(headerUserId.equals(userId) && dto.getUserId().equals(userId))) {
            throw new NotEqualUserIdException();
        }

        return true;
    }

}
