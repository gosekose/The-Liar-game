package liar.gamemvcservice.game.service;

import liar.gamemvcservice.game.domain.Topic;
import org.springframework.stereotype.Component;

@Component
public interface TopicPolicy {
    Topic setUp();
}
