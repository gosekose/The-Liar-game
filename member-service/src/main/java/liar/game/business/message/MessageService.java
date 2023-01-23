package liar.game.business.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Long testValueService(Long id) {

        return messageRepository.testValueRepository(id);
    }

}
