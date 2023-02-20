package liar.gamemvcservice.game.controller;

import liar.gamemvcservice.exception.exception.NotEqualUserIdException;
import liar.gamemvcservice.game.controller.dto.message.ChatMessage;
import liar.gamemvcservice.game.controller.dto.message.message.ChatMessageResponse;
import liar.gamemvcservice.game.domain.NextTurn;
import liar.gamemvcservice.game.service.GameFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-service/game")
public class ChatController {

    private final GameFacadeService gameFacadeService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/{gameId}")
    public void chatTopic(@DestinationVariable String gameId,
                          StompHeaderAccessor headerAccessor,
                          ChatMessage message) throws InterruptedException {

        isMatchUserIdAndRequestMessageUserId(headerAccessor, message);
        NextTurn nextTurn = gameFacadeService
                .updateAndInformPlayerTurn(gameId, message.getCharMessage());

        template.convertAndSend("/topic/" + gameId, ChatMessageResponse.of(message, nextTurn));
    }

    private boolean isMatchUserIdAndRequestMessageUserId(SimpMessageHeaderAccessor headerAccessor, ChatMessage message) {
        String userId = headerAccessor.getFirstNativeHeader("userId");
        if (userId.equals(message.getUserId())) {
            return true;
        }
        throw new NotEqualUserIdException();
    }
}
