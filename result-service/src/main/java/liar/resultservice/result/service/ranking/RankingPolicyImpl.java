package liar.resultservice.result.service.ranking;

import liar.resultservice.result.repository.query.rank.PlayerRankingQueryDslRepository;
import liar.resultservice.result.service.dto.PlayerRankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class RankingPolicyImpl implements RankingPolicy {

    private final PlayerRankingQueryDslRepository playerRankingQueryDslRepository;

    @Override
    public Slice<PlayerRankingDto> fetchPlayerRanking(Pageable pageable) {
        return playerRankingQueryDslRepository.fetchPlayerRanking(pageable);
    }
}
