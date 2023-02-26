package liar.resultservice.result.service.dto;

import jakarta.validation.constraints.NotNull;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.controller.dto.request.VotedResultDto;
import liar.resultservice.result.domain.GameRole;
import lombok.AllArgsConstructor;
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

    public SaveResultDto(SaveResultRequest request) {
        this.gameId = request.getGameId();
        this.winner = request.getWinner();
        this.playersInfo = new CopyOnWriteArrayList<>(request.getPlayersInfo());
        this.roomId = request.getRoomId();
        this.gameName = request.getGameName();
        this.hostId = request.getHostId();
        this.topicId = request.getTopicId();
        this.totalUserCnt = request.getTotalUserCnt();
        this.votedResults = new CopyOnWriteArrayList<>(request.getVotedResults());
    }
}
