package liar.game.business.channel.repository.condtion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnterChannelCondition {
    private String channelName;
    private String hostName;
}

