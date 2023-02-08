package liar.gameservice.game.service.policy;

import liar.gameservice.game.controller.dto.request.JoinMemberDto;
import liar.gameservice.game.service.TopicService;
import liar.gameservice.game.service.dao.TopicName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TopicPolicyImpl implements TopicPolicy<JoinMemberDto> {

    private final TopicService topicService;

    @Override
    public HashMap<String, String> sendSubjectToMember(JoinMemberDto joinMemberDto) {
        Map<String, String> map = new HashMap<>();
        TopicName topicName = topicService.findDictionaryNameOnly();
//        int liarIndex = (int) (Math.random() * joinMemberDto.getMemberIds().size()) + 1;
        return null;
    }

    @Override
    public void redefineSubjectWhenTheLiarExits(JoinMemberDto joinMemberDto) {

    }
}
