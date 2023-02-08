package liar.gameservice.game.service;

import liar.gameservice.game.repository.TopicRepository;
import liar.gameservice.game.service.dao.TopicName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicService {


    @Value("${topic.range}")
    private long range;

    private final TopicRepository topicRepository;

    @Transactional(readOnly = true)
    public TopicName findDictionaryNameOnly() {
        TopicName result;
        long id = ((long) Math.random() * range) + 1;

        while ((result = topicRepository.findNameOnlyById(id)) == null) {
            id = ((long) Math.random() * range) + 1;
        }

        return result;
    }


}
