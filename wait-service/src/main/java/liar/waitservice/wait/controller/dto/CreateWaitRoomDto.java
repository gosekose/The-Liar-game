package liar.waitservice.wait.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateWaitRoomDto {

    private String userId;
    private String roomName;
    private int limitMembers;

    @Builder
    public CreateWaitRoomDto(String userId, String roomName, int limitMembers) {
        this.userId = userId;
        this.roomName = roomName;
        this.limitMembers = limitMembers;
    }
}
