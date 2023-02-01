package liar.waitservice.wait.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("waitRoomName")
public class WaitRoomName implements Serializable {

    @Id
    private String id;
    private List<String> roomIds = new LinkedList<>();

    public WaitRoomName(WaitRoom waitRoom) {
        this.id = waitRoom.getRoomName();
        addWaitRoomIds(waitRoom.getId());
    }

    public boolean addWaitRoomIds(String waitRoomId) {
        int size = roomIds.size();
        roomIds.add(waitRoomId);
        return isWaitRoomIdsStatusChange(size);
    }

    public boolean removeWaitRoomIds(String waitRoomId) {
        int size = roomIds.size();
        roomIds.remove(waitRoomId);
        return isWaitRoomIdsStatusChange(size);
    }

    private boolean isWaitRoomIdsStatusChange(int size) {
        if (size != roomIds.size()) {
            return true;
        }
        return false;
    }
}
