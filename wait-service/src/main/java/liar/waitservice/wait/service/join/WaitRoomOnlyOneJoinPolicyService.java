package liar.waitservice.wait.service.join;

import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.utils.WaitRoomCompleteStatus;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteRepository;
import liar.waitservice.wait.repository.redis.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class WaitRoomOnlyOneJoinPolicyService implements WaitRoomJoinPolicyService {

    private final WaitRoomRedisRepository waitRoomRedisRepository;
    private final JoinMemberRedisRepository joinMemberRedisRepository;
    private final WaitRoomCompleteRepository waitRoomCompleteRepository;

    @Override
    public void createWaitRoomPolicy(String hostId) {
        createAndManageOnlyOneRoomPerHostAtTheSameTime(hostId);
    }

    @Override
    public void joinWaitRoomPolicy(String userId) {
        joinOnlyOneRoomPerMemberAtTheSameTime(userId);
    }

    @Override
    public boolean isNotPlayingUser(String userId) {
        List<WaitRoomComplete> waitRoomCompletes = waitRoomCompleteRepository.findByHostId(userId)
                .stream().filter(f -> f.getWaitRoomCompleteStatus().equals(WaitRoomCompleteStatus.PLAYING))
                .collect(Collectors.toList());

        if (waitRoomCompletes.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isNotPlayingWaitRoom(String waitRoomId) {
        WaitRoom waitRoom = waitRoomRedisRepository.findById(waitRoomId).orElseThrow(NotFoundWaitRoomException::new);
        return waitRoom.isWaiting();
    }

    /**
     * waitRoom은 같은 시간에 오직 하나의 방만 개설하여 운영할 수 있다.
     */
    private void createAndManageOnlyOneRoomPerHostAtTheSameTime(String hostId) {
        WaitRoom waitRoom = waitRoomRedisRepository.findByHostId(hostId);
        if (waitRoom != null) {
            deleteWaitRoomAndAllJoinMemberStatusLeave(waitRoom);
        }
    }

    /**
     * user는 동시에 두개 이상의 방을 접속할 수 없다.
     * 이미 다른 곳의 호스트가 다른 방에 입장 요청을 하면, 이전에 있던 방의 유저들의 join을 제거하고 소유하고 있는 방을 나간다.
     */
    private void joinOnlyOneRoomPerMemberAtTheSameTime(String userId) {

        JoinMember joinMember = joinMemberRedisRepository.findById(userId).orElse(null);

        if (joinMember != null) {

            WaitRoom waitRoom = waitRoomRedisRepository.findById(joinMember.getRoomId()).orElseThrow(NotFoundWaitRoomException::new);

            if (waitRoom.isHost(joinMember.getId())) {
                deleteWaitRoomAndAllJoinMemberStatusLeave(waitRoom);

            } else {
                waitRoom.leaveMember(userId);
                waitRoomRedisRepository.save(waitRoom);
                joinMemberRedisRepository.delete(joinMember);
            }
        }
    }
    private void deleteWaitRoomAndAllJoinMemberStatusLeave(WaitRoom waitRoom) {
        waitRoom.getMembers().stream().forEach(j -> joinMemberRedisRepository.delete(JoinMember.of(j, waitRoom.getId())));
        waitRoomRedisRepository.delete(waitRoom);
    }

}
