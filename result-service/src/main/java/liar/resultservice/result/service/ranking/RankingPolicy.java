package liar.resultservice.result.service.ranking;

import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public interface RankingPolicy {

    Slice<PlayerRankingDto> fetchPlayerRanking(Pageable pageable);

}
