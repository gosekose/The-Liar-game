package liar.game.business.channel.service;

import liar.game.business.channel.domain.Channel;
import liar.game.business.channel.repository.ChannelQueryRepository;
import liar.game.business.channel.repository.ChannelRepository;
import liar.game.business.channel.repository.condtion.EnterChannelCondition;
import liar.game.business.channel.repository.dto.EnterChannelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelQueryRepository channelQueryRepository;

    @Transactional
    public Long save(Channel channel) {
        Channel savedChannel = channelRepository.save(channel);
        return savedChannel.getId();
    }

    public Page<EnterChannelDto> getEnterChannels(EnterChannelCondition condition, Pageable pageable) {
        return channelQueryRepository.getEnterChannelDtoPageComplex(condition, pageable);
    }

    public Channel findOne(Long id) {
        return channelRepository.findById(id)
                .orElseThrow(
                        () -> {
                            throw new IllegalArgumentException("일치하는 회원이 없습니다.");
                        }
                );

    }

}
