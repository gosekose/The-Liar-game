package liar.resultservice.common.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import liar.resultservice.result.service.ResultFacadeService;
import liar.resultservice.result.service.dto.SaveResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MySQSMessageListener {

    private final AmazonSQS amazonSQS;
    private final ObjectMapper objectMapper;
    private final ResultFacadeService resultFacadeService;

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @PostConstruct
    public void startListening() {
        new Thread(() -> {

            while (true) {
                List<Message> messages = amazonSQS.receiveMessage(new ReceiveMessageRequest(queueUrl)
                                .withWaitTimeSeconds(60)
                                .withMaxNumberOfMessages(10)
                                .withVisibilityTimeout(60))
                        .getMessages();

                messages.stream()
                        .forEach(message -> {
                            try {
                                SaveResultDto saveResultDto = objectMapper.readValue(message.getBody(), SaveResultDto.class);
                                resultFacadeService.saveAllResultOfGame(saveResultDto);

                                String receiptHandle = message.getReceiptHandle();
                                amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, receiptHandle));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

            }
        }).start();
    }
}
