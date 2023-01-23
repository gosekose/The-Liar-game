package liar.game.business.channel.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import liar.game.business.channel.repository.condtion.EnterChannelCondition;
import liar.game.business.channel.repository.dto.EnterChannelDto;
import liar.game.business.channel.repository.dto.QEnterChannelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static liar.game.business.channel.domain.QChannel.channel;
import static liar.game.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class ChannelQueryRepositoryImpl implements ChannelQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<EnterChannelDto> getEnterChannelDtoPageComplex(EnterChannelCondition condition, Pageable pageable) {

        List<EnterChannelDto> content = query
                .select(
                        new QEnterChannelDto(
                                channel.id,
                                channel.channelName,
                                channel.host.username,
                                channel.maxMember
                        )
                )
                .from(channel)
                .join(channel.host, member)
                .where(
                        channelNameContains(condition.getChannelName()),
                        hostNameContains(condition.getHostName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(channel.count())
                .from(channel)
                .where(
                        channelNameContains(condition.getChannelName()),
                        hostNameContains(condition.getHostName())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression channelNameContains(String channelName) {
        return channelName != null ? channel.channelName.contains(channelName) : null;
    }

    private BooleanExpression hostNameContains(String hostName) {
        return hostName != null ? channel.host.username.contains(hostName) : null;
    }


}
