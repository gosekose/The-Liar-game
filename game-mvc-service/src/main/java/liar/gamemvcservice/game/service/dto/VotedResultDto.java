package liar.gamemvcservice.game.service.dto;

import liar.gamemvcservice.game.domain.VotedResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VotedResultDto {

    private String liarId;
    private List<String> userIds = new ArrayList<>();
    private int cnt;

    public VotedResultDto(VotedResult votedResult) {
        this.liarId = votedResult.getLiarId();
        this.userIds = votedResult.getUserIds();
        this.cnt = votedResult.getCnt();
    }
}
