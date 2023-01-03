package liar.game.business.channel.repository.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * liar.game.business.channel.repository.dto.QEnterChannelDto is a Querydsl Projection type for EnterChannelDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QEnterChannelDto extends ConstructorExpression<EnterChannelDto> {

    private static final long serialVersionUID = 1916256702L;

    public QEnterChannelDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> channelName, com.querydsl.core.types.Expression<String> hostName, com.querydsl.core.types.Expression<Integer> maxMember) {
        super(EnterChannelDto.class, new Class<?>[]{long.class, String.class, String.class, int.class}, id, channelName, hostName, maxMember);
    }

}

