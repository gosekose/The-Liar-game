package liar.gameservice.game.service.policy;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public interface TopicPolicy<T> {

    HashMap<String, String> sendSubjectToMember(T t);
    void redefineSubjectWhenTheLiarExits(T t);
}
