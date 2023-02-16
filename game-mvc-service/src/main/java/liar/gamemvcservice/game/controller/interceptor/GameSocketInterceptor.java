package liar.gamemvcservice.game.controller.interceptor;

import liar.gamemvcservice.exception.exception.BindingInvalidException;
import liar.gamemvcservice.exception.exception.NotEqualUserIdException;
import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.controller.ChatMessage;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.service.GameService;
import liar.gamemvcservice.game.service.PlayGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class GameSocketInterceptor implements ChannelInterceptor {

    private final PlayGameService playGameService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            isValidateGameIdAndUserId(headerAccessor);
        }

        return message;
    }

    private boolean isValidateGameIdAndUserId(StompHeaderAccessor headerAccessor) {

        String userId = headerAccessor.getFirstNativeHeader("userId");
        String gameId = headerAccessor.getFirstNativeHeader("gameId");

        if(userId != null && gameId != null) {
            if (playGameService.findJoinMemberOfRequestGame(gameId, userId) != null) {
                return true;
            }
        }

        throw new BindingInvalidException();
    }
}
