package liar.waitservice.wait.domain;

import jakarta.persistence.*;
import liar.waitservice.wait.domain.utils.BaseTimeEntity;
import liar.waitservice.wait.domain.utils.WaitRoomStatus;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Table(indexes = {@Index(name = "wait_room_index",columnList = "wait_room_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitRoomComplete extends BaseTimeEntity {

    @Id @Generated
    @Column(name = "wait_room_complete_id")
    private Long id;

    @Column(name = "wait_room_id")
    private String waitRoomId;

    private String roomName;

    private String hostId;

    private String hostName;

    private int limitMembers;

    @Enumerated(STRING)
    private WaitRoomStatus waitRoomStatus;

    protected WaitRoomComplete(WaitRoom waitRoom) {
        this.waitRoomId = waitRoom.getId();
        this.roomName = waitRoom.getRoomName();
        this.hostId = waitRoom.getHostId();
        this.hostName = waitRoom.getHostName();
        this.limitMembers = waitRoom.getLimitMembers();
        this.waitRoomStatus = WaitRoomStatus.PLAYING;
    }

    public static WaitRoomComplete of(WaitRoom waitRoom) {
        return new WaitRoomComplete(waitRoom);
    }

    public void updateWaitRoomStatusDueToEndGame() {
        waitRoomStatus = WaitRoomStatus.END;
    }
}
