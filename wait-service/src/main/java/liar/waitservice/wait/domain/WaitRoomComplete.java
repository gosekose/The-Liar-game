package liar.waitservice.wait.domain;

import jakarta.persistence.*;
import liar.waitservice.wait.domain.utils.BaseTimeEntity;
import liar.waitservice.wait.domain.utils.WaitRoomCompleteStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Table(indexes = {@Index(name = "wait_room_index",columnList = "wait_room_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitRoomComplete extends BaseTimeEntity {

    @Id
    @Column(name = "wait_room_complete_id")
    private String id;

    @Column(name = "wait_room_id")
    private String waitRoomId;

    private String roomName;

    private String hostId;

    private String hostName;

    private int limitMembers;

    @Enumerated(STRING)
    private WaitRoomCompleteStatus waitRoomCompleteStatus;

    protected WaitRoomComplete(WaitRoom waitRoom) {
        this.id = UUID.randomUUID().toString();
        this.waitRoomId = waitRoom.getId();
        this.roomName = waitRoom.getRoomName();
        this.hostId = waitRoom.getHostId();
        this.hostName = waitRoom.getHostName();
        this.limitMembers = waitRoom.getLimitMembers();
        this.waitRoomCompleteStatus = WaitRoomCompleteStatus.PLAYING;
    }

    public static WaitRoomComplete of(WaitRoom waitRoom) {
        return new WaitRoomComplete(waitRoom);
    }

    public void updateWaitRoomStatusDueToEndGame() {
        waitRoomCompleteStatus = WaitRoomCompleteStatus.END;
    }
}
