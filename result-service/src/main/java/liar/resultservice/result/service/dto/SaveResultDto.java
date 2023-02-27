package liar.resultservice.result.service.dto;

import jakarta.validation.constraints.NotNull;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.controller.dto.request.VotedResultDto;
import liar.resultservice.result.domain.GameRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@AllArgsConstructor
@NotNull
public class SaveResultDto {
    private String gameId;
    private GameRole winner;
    private CopyOnWriteArrayList<PlayerResultInfoDto> playersInfo;
    private String roomId;
    private String gameName;
    private String hostId;
    private Long topicId;
    private int totalUserCnt;
    private CopyOnWriteArrayList<VotedResultDto> votedResults;

    @Builder
    public SaveResultDto(String gameId, GameRole winner, List<PlayerResultInfoDto> playersInfo,
                         String roomId, String gameName, String hostId, Long topicId,
                         int totalUserCnt, List<VotedResultDto> votedResults) {
        this.gameId = gameId;
        this.winner = winner;
        this.playersInfo = new CopyOnWriteArrayList<>(playersInfo);
        this.roomId = roomId;
        this.gameName = gameName;
        this.hostId = hostId;
        this.topicId = topicId;
        this.totalUserCnt = totalUserCnt;
        this.votedResults = new CopyOnWriteArrayList<>(votedResults);
    }
}
