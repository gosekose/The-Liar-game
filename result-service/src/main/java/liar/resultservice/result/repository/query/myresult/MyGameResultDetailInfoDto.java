package liar.resultservice.result.repository.query.myresult;

import liar.resultservice.result.domain.GameRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyGameResultDetailInfoDto {

    private String userId;
    private String gameId;
    private String gameName;
    private String topicName;
    private GameRole winner;
    private Integer totalUsers;
    private GameRole myRole;
    private Boolean answer;

}
