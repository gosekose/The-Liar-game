package liar.gameservice.other.waitroomcomplete;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitRoomComplete {

    @Id
    @GeneratedValue
    @Column(name = "wait_room_complete_id")
    private Long id;

    @Column(name = "wait_room_id")
    private String waitRoomId;

    private String roomName;

    private String hostId;

    private String hostName;

    private int limitMembers;

}
