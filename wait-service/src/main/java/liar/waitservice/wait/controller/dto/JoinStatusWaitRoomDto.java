package liar.waitservice.wait.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinStatusWaitRoomDto {

    private String userId;
    private String roomId;

}
