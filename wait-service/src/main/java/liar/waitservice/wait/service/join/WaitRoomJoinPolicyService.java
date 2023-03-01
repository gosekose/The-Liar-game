package liar.waitservice.wait.service.join;

import org.springframework.stereotype.Component;

@Component
public interface WaitRoomJoinPolicyService {

    void createWaitRoomPolicy(String hostId);
    void joinWaitRoomPolicy(String userId);

    boolean isNotPlayingUser(String userId);
    boolean isNotPlayingWaitRoom(String waitRoomId);

}
