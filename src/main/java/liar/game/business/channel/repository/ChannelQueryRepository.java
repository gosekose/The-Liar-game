package liar.game.business.channel.repository;

import liar.game.business.channel.repository.condtion.EnterChannelCondition;
import liar.game.business.channel.repository.dto.EnterChannelDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelQueryRepository {

    Page<EnterChannelDto> getEnterChannelDtoPageComplex(EnterChannelCondition condition, Pageable pageable);

}
