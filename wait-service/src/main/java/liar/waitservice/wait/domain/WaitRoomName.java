package liar.waitservice.wait.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.LinkedList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("waitRoomName")
public class WaitRoomName {

    @Id
    private String id;
    private List<String> roomIds = new LinkedList<>();

    public WaitRoomName(WaitRoom waitRoom) {
        this.id = waitRoom.getRoomName();
        addWaitRoomIds(waitRoom.getId());
    }

    public void addWaitRoomIds(String waitRoomId) {
        roomIds.add(waitRoomId);
    }

    public void removeWaitRoomIds(String waitRoomId) {
        roomIds.remove(waitRoomId);
    }
}
