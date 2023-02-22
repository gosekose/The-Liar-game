package liar.resultservice.result.repository.query.myresult;

import com.querydsl.core.annotations.QueryProjection;
import liar.resultservice.result.domain.GameRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyDetailGameResultDto {
    private String gameId;
    private String gameName;
    private String topicName;
    private GameRole winner;
    private Integer totalUsers;
    private GameRole myRole;
    private Boolean answer;

    @QueryProjection
    public MyDetailGameResultDto(String gameId, String gameName, String topicName, GameRole winner, Integer totalUsers, GameRole myRole, Boolean answer) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.topicName = topicName;
        this.winner = winner;
        this.totalUsers = totalUsers;
        this.myRole = myRole;
        this.answer = answer;
    }
}
