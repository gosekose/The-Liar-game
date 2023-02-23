package liar.resultservice.result.controller.dto.request;

import liar.resultservice.result.domain.GameRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResultInfoDto {

    private String userId;
    private GameRole gameRole;
    private Boolean answers;

}