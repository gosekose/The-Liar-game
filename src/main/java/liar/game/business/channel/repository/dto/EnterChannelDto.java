package liar.game.business.channel.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnterChannelDto {

    private Long id;
    private String channelName;
    private String hostName;
    private int maxMember;

    @Builder
    @QueryProjection
    public EnterChannelDto(Long id, String channelName, String hostName, int maxMember) {
        this.id = id;
        this.channelName = channelName;
        this.hostName = hostName;
        this.maxMember = maxMember;
    }
}
