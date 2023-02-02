package liar.waitservice.wait.service.policy;

import org.springframework.stereotype.Component;

@Component
public interface WaitRoomJoinPolicyService {

    void createWaitRoomPolicy(String hostId);
    void joinWaitRoomPolicy(String userId);

}
