package liar.waitservice.wait.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinStatusWaitRoomDto {

    private String userId;
    private String roomId;

}
