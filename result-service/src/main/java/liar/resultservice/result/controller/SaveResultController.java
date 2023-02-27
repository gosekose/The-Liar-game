package liar.resultservice.result.controller;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.service.ResultFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/result-service/result")
@RequiredArgsConstructor
public class SaveResultController {

    private final AmazonSQSAsync amazonSQS;
    private final ResultFacadeService resultFacadeService;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @PostMapping
    public ResponseEntity<Void> saveResults(@RequestBody SaveResultRequest request) {
        try {
            String messageBody = objectMapper.writeValueAsString(request);
            amazonSQS.sendMessageAsync(new SendMessageRequest(queueUrl, messageBody));
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
