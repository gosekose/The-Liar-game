package liar.gamemvcservice.game.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleController {

    private final AmazonSQS amazonSQS;
    private final MessageHandler messageHandler;

    @Value("${aws.sqs.queueUrl}")
    private String sqsQueueUrl;

    @Scheduled(fixedRate = 1000)
    public void receiveMessages() {
        ReceiveMessageRequest request = new ReceiveMessageRequest()
                .withQueueUrl(sqsQueueUrl)
                .withMaxNumberOfMessages(10)
                .withWaitTimeSeconds(20);
        List<Message> messages = amazonSQS.receiveMessage(request).getMessages();
        for (Message message : messages) {
//            messageHandler.handleMessage(message.getBody());
            amazonSQS.deleteMessage(sqsQueueUrl, message.getReceiptHandle());
        }
    }
}
