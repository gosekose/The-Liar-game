package liar.waitservice.wait.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateWaitRoomStatusDto<T> {
    private T roomId;
}
