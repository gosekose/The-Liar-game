package liar.gamemvcservice.game.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import liar.gamemvcservice.game.controller.dto.message.SendSuccessBody;
import liar.gamemvcservice.game.controller.dto.request.CommonRequest;
import liar.gamemvcservice.game.service.GameFacadeService;
import liar.gamemvcservice.game.service.dto.GameResultToServerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("game-service/game")
public class ResultController {

    private final GameFacadeService gameFacadeService;
    private final AmazonSQS amazonSQS;

    @Value("${aws.sqs.queueName}")
    private String queueName;

    @GetMapping("/result")
    public ResponseEntity getGameResultInformation(CommonRequest dto) {

        messageGameResultToServerIfFirstMessage(dto);

        return ResponseEntity.ok()
                .body(SendSuccessBody.of(gameFacadeService
                        .sendGameResultToClient(dto.getGameId())));
    }

    private void messageGameResultToServerIfFirstMessage(CommonRequest dto) {
        GameResultToServerDto gameResultToServerDto = gameFacadeService
                .sendGameResultToServer(dto.getGameId());

        if (gameResultToServerDto != null) {
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(getQueueUrl())
                    .withMessageBody(gameResultToServerDto.toString());
            amazonSQS.sendMessage(sendMessageRequest);
        }
    }

    private String getQueueUrl() {
        GetQueueUrlResult queueUrlResult = amazonSQS.getQueueUrl(queueName);
        return queueUrlResult.getQueueUrl();
    }

}
