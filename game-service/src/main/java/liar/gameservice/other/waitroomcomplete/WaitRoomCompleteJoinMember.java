package liar.gameservice.other.waitroomcomplete;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitRoomCompleteJoinMember {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wait_room_complete_id")
    private WaitRoomComplete waitRoomComplete;

    private String userId;

}
