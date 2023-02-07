package liar.gameservice.game.repository;

import liar.gameservice.game.domain.Dictionary;
import liar.gameservice.game.service.dao.DictionaryNameOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    DictionaryNameOnly findNameOnlyById(Long id);
}
