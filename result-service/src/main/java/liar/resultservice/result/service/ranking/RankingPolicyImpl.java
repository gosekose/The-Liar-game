package liar.resultservice.result.service.ranking;

import liar.resultservice.result.repository.query.rank.PlayerRankingQueryDslRepository;
import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankingPolicyImpl implements RankingPolicy {

    private final PlayerRankingQueryDslRepository repository;

    @Override
    public Slice<PlayerRankingDto> fetchPlayerRanking(Pageable pageable) {
        return repository.fetchPlayerRanking(pageable);
    }
}
