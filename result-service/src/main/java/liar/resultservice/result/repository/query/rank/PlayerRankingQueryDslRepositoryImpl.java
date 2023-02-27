package liar.resultservice.result.repository.query.rank;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static liar.resultservice.other.member.QMember.member;
import static liar.resultservice.result.domain.QPlayer.player;

@Repository
@RequiredArgsConstructor
public class PlayerRankingQueryDslRepositoryImpl implements PlayerRankingQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public Slice<PlayerRankingDto> fetchPlayerRanking(Pageable pageable) {
        List<PlayerRankingDto> content = query
                .select(
                        new liar.resultservice.result.repository.query.rank.QPlayerRankingDto(
                                member.userId,
                                member.username,
                                player.level,
                                player.exp,
                                player.wins,
                                player.loses,
                                player.totalGames)
                )
                .from(player)
                .join(player.member, member)
                .where(
                        player.visibleGameResult.isTrue()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(player.exp.desc())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(player.count())
                .from(player)
                .where(
                        player.visibleGameResult.isTrue()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}

