package liar.resultservice.result.repository.query.myresult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyDetailGameResultCond {

    private String userId;
    private Boolean viewLatest;
    private Boolean viewOnlyWin;
    private Boolean viewOnlyLose;
    private String searchGameName;
}
