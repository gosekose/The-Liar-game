package liar.resultservice.result.repository.query.myresult;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static liar.resultservice.other.member.QMember.member;
import static liar.resultservice.other.topic.QTopic.topic;
import static liar.resultservice.result.domain.QGameResult.*;
import static liar.resultservice.result.domain.QPlayer.player;
import static liar.resultservice.result.domain.QPlayerResult.playerResult;

@Repository
@RequiredArgsConstructor
public class MyDetailGameResultQueryDslRepositoryImpl implements MyDetailGameResultQueryDslRepository{

    private final JPAQueryFactory query;

    @Override
    public Slice<MyDetailGameResultDto> fetchMyDetailGameResult(MyDetailGameResultCond cond, Pageable pageable) {

        List<MyDetailGameResultDto> content = selectMyDetailGameResultContent(cond, pageable);
        JPAQuery<Long> countQuery = countMyDetailGameResult(cond);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private List<MyDetailGameResultDto> selectMyDetailGameResultContent(MyDetailGameResultCond cond, Pageable pageable) {
        return query
                .select(
                        new liar.resultservice.result.repository.query.myresult.QMyDetailGameResultDto(
                                gameResult.gameId,
                                gameResult.gameName,
                                gameResult.topic.topicName,
                                gameResult.winner,
                                gameResult.totalUsers,
                                playerResult.gameRole,
                                playerResult.answers
                        )
                )
                .from(playerResult)
                .join(topic, gameResult.topic)
                .join(member, player.member)
                .join(playerResult.gameResult, gameResult)
                .where(
                        member.userId.eq(cond.getUserId()),
                        playerWinEq(cond.getViewOnlyWin()),
                        playerLoseEq(cond.getViewOnlyLose()),
                        gameNameContains(cond.getSearchGameName())
                )
                .orderBy(createOrderSpecifier(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPAQuery<Long> countMyDetailGameResult(MyDetailGameResultCond cond) {

        if (cond.getSearchGameName() == null) {
            return query
                    .select(playerResult.count())
                    .from(playerResult)
                    .join(member, player.member)
                    .where(
                            member.userId.eq(cond.getUserId()),
                            playerWinEq(cond.getViewOnlyWin()),
                            playerLoseEq(cond.getViewOnlyLose())
                    );
        }
        else {
            return query
                    .select(playerResult.count())
                    .from(playerResult)
                    .join(member, player.member)
                    .join(playerResult.gameResult, gameResult)
                    .where(
                            member.userId.eq(cond.getUserId()),
                            gameNameContains(cond.getSearchGameName())
                    );
        }
    }

    private BooleanExpression gameNameContains(String searchGameName) {
        return searchGameName != null ? gameResult.gameName.contains(searchGameName) : null;
    }

    private BooleanExpression playerWinEq(Boolean viewOnlyWin) {
        return viewOnlyWin != null ? playerResult.isWin.eq(true) : null;
    }

    private BooleanExpression playerLoseEq(Boolean viewOnlyLose) {
        return viewOnlyLose != null ? playerResult.isWin.eq(false) : null;
    }

    private OrderSpecifier[] createOrderSpecifier(MyDetailGameResultCond cond) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (cond.getViewLatest() != null) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, gameResult.createdAt));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

}
