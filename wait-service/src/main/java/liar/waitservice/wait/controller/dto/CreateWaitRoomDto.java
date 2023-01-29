package liar.waitservice.wait.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateWaitRoomDto {

    private String userId;
    private String roomName;
    private int limitMembers;


}
