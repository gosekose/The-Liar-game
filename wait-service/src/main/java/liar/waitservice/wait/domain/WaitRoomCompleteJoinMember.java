package liar.waitservice.wait.domain;

import jakarta.persistence.*;
import liar.waitservice.wait.domain.utils.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitRoomCompleteJoinMember extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wait_room_complete_id")
    private WaitRoomComplete waitRoomComplete;

    private String userId;

    protected WaitRoomCompleteJoinMember(WaitRoomComplete waitRoomComplete, String userId) {
        this.waitRoomComplete = waitRoomComplete;
        this.userId = userId;
    }

    public static WaitRoomCompleteJoinMember of(WaitRoomComplete waitRoomComplete, String userId) {
        return new WaitRoomCompleteJoinMember(waitRoomComplete, userId);
    }


}
