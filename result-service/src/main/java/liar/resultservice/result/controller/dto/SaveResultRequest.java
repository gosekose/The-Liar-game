package liar.resultservice.result.controller.dto;

import liar.resultservice.result.domain.GameRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveResultRequest {
    private String gameId;
    private GameRole winner;
    private List<PlayerResultInfoDto> playersInfo;
    private String roomId;
    private String gameName;
    private String hostId;
    private Long topicId;
    private int totalUserCnt;
    private List<VotedResultDto> votedResults;
}
