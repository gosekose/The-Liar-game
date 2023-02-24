package liar.gamemvcservice.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VotedResult {
    private String liarId;
    private List<String> userIds = new ArrayList<>();
    private int cnt;

    public VotedResult(String liarId, int cnt) {
        this.liarId = liarId;
        this.cnt = cnt;
    }

    public void addUserId(String userId) {
        userIds.add(userId);
        cnt++;
    }
}
