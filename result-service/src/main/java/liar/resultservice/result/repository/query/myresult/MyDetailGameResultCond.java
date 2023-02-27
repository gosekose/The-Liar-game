package liar.resultservice.result.repository.query.myresult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyDetailGameResultCond {

    private String userId;
    private Boolean viewLatest;
    private Boolean viewOnlyWin;
    private Boolean viewOnlyLose;
    private String searchGameName;

    @Builder
    public MyDetailGameResultCond(String userId, Boolean viewLatest, Boolean viewOnlyWin, Boolean viewOnlyLose, String searchGameName) {
        this.userId = userId;
        this.viewLatest = viewLatest;
        this.viewOnlyWin = viewOnlyWin;
        this.viewOnlyLose = viewOnlyLose;
        this.searchGameName = searchGameName;
    }
}
