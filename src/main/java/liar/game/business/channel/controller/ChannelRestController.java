package liar.game.business.channel.controller;

import liar.game.business.channel.controller.dto.FindOneChannelDto;
import liar.game.business.channel.domain.Channel;
import liar.game.business.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/channel")
public class ChannelRestController {

    private final ChannelService channelService;


    @RequestMapping("/search")
    public ResponseEntity findOneChannel(FindOneChannelDto findOneChannelDto) {
        Channel channel = channelService.findOne(findOneChannelDto.getId());
        return new ResponseEntity(channel, HttpStatus.OK);
    }

}
