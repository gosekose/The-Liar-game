package liar.gameservice.game.service;

import liar.gameservice.game.repository.DictionaryRepository;
import liar.gameservice.game.service.dao.DictionaryNameOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class DictionaryService {


    @Value("${dictionary.range}")
    private long range;

    private final DictionaryRepository dictionaryRepository;

    @Transactional(readOnly = true)
    public Mono<DictionaryNameOnly> findDictionaryNameOnlyById() {
        DictionaryNameOnly result;
        long id = ((long) Math.random() * range) + 1;

        while ((result = dictionaryRepository.findNameOnlyById(id)) == null) {
            id = ((long) Math.random() * range) + 1;
        }

        return Mono.just(result);
    }


}
