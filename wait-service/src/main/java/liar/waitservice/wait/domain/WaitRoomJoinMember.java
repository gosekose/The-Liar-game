package liar.waitservice.wait.domain;

import jakarta.persistence.*;
import liar.waitservice.wait.domain.utils.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitRoomJoinMember extends BaseTimeEntity {

    @Id @Generated
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wait_room_complete_id")
    private WaitRoomComplete waitRoomComplete;

    private String userId;

    protected WaitRoomJoinMember(WaitRoomComplete waitRoomComplete, String userId) {
        this.waitRoomComplete = waitRoomComplete;
        this.userId = userId;
    }

    public static WaitRoomJoinMember of(WaitRoomComplete waitRoomComplete, String userId) {
        return new WaitRoomJoinMember(waitRoomComplete, userId);
    }


}
