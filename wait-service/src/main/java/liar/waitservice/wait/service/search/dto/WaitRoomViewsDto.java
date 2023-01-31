package liar.waitservice.wait.service.search.dto;

import liar.waitservice.wait.domain.WaitRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitRoomViewsDto {

    private String roomId;
    private String hostId;
    private String hostName;
    private int limitsMembers;
    private int joinMembersCnt;

    public WaitRoomViewsDto(WaitRoom waitRoom) {
        this.roomId = waitRoom.getId();
        this.hostId = waitRoom.getHostId();
        this.hostName = waitRoom.getHostName();
        this.limitsMembers = waitRoom.getLimitMembers();
        this.joinMembersCnt = waitRoom.getMembers().size();
    }


}
