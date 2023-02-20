package liar.gamemvcservice.game.controller.interceptor;

import liar.gamemvcservice.exception.exception.BindingInvalidException;
import liar.gamemvcservice.game.service.GameFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatInterceptor implements ChannelInterceptor {

    private final GameFacadeService gameFacadeService;

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
            if (gameFacadeService.findJoinPlayer(gameId, userId) != null) {
                return true;
            }
        }

        throw new BindingInvalidException();
    }
}
