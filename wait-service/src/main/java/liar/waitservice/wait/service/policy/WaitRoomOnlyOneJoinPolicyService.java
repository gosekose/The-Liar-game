package liar.waitservice.wait.service.policy;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.WaitRoomRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitRoomOnlyOneJoinPolicyService implements WaitRoomJoinPolicyService {

    private final WaitRoomRedisRepository waitRoomRedisRepository;
    private final JoinMemberRedisRepository joinMemberRedisRepository;

    @Override
    public void createWaitRoomPolicy(String hostId) {
        createAndManageOnlyOneRoomPerHostAtTheSameTime(hostId);
    }

    @Override
    public void joinWaitRoomPolicy(String userId) {
        joinOnlyOneRoomPerMemberAtTheSameTime(userId);
    }

    /**
     * waitRoom은 같은 시간에 오직 하나의 방만 개설하여 운영할 수 있다.
     */
    private void createAndManageOnlyOneRoomPerHostAtTheSameTime(String hostId) {
        WaitRoom waitRoom = waitRoomRedisRepository.findByHostId(hostId);
        if (waitRoom != null) {
            waitRoomRedisRepository.delete(waitRoom);
        }
    }

    /**
     * user는 동시에 두개 이상의 방을 접속할 수 없다.
     */
    private void joinOnlyOneRoomPerMemberAtTheSameTime(String userId) {
        JoinMember joinMember = joinMemberRedisRepository.findJoinMembersById(userId);
        if (joinMember != null) {
            WaitRoom waitRoom = waitRoomRedisRepository.findById(joinMember.getRoomId()).orElseThrow(NotExistsRoomIdException::new);
            waitRoom.leaveMember(userId);
            waitRoomRedisRepository.save(waitRoom);
            joinMemberRedisRepository.delete(joinMember);
        }
    }
}
