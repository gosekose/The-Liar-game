package liar.resultservice.result.repository.query.rank;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRankingQueryDslRepository {

    Slice<PlayerRankingDto> fetchPlayerRanking(Pageable pageable);

}
