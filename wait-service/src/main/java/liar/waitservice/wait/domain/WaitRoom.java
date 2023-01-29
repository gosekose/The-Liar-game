package liar.waitservice.wait.domain;

import jakarta.persistence.Id;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
@RedisHash("waitRoom")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitRoom {

    @Id
    private String id;

    private String roomName;

    private String hostId;

    private String roomId;

    private int limitMembers;
    private List<String> members = new LinkedList<>();

    private LocalDateTime createdAt;

    protected WaitRoom (CreateWaitRoomDto createWaitRoomDto) {
        this.roomName = createWaitRoomDto.getRoomName();
        this.hostId = createWaitRoomDto.getUserId();
        this.limitMembers = createWaitRoomDto.getLimitMembers();
        this.roomId = UUID.randomUUID().toString();
        members.add(hostId);
    }

    public static WaitRoom of(CreateWaitRoomDto createWaitRoomDto) {
        return new WaitRoom(createWaitRoomDto);
    }

    /**
     * 대기방 인원이 여유가 있고, 요청이 호스트가 아니라면 회원 추가
     */
    public boolean addMembers(String userId) {

        if (isNotFullMembers() || !isHost(userId)) {
            members.add(userId);
            return true;
        }
        return false;
    }

    /**
     * 대기방에 있는 유저 나가기
     */
    public void leaveMembers(String userId) {
        if (!isHost(userId)) members.remove(userId);
    }

    /**
     * 요청 유저 아이가 방의 호스트와 같은지 파악
     */
    public boolean isHost(String userId) {
        return userId.equals(this.hostId);
    }

    /**
     * 대기방 만석 파악
     */
    private boolean isNotFullMembers() {
        return members.size() < limitMembers;
    }

}
