package liar.resultservice.result.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyDetailGameResultRequest {
    private String userId;
    private Boolean viewLatest;
    private Boolean viewOnlyWin;
    private Boolean viewOnlyLose;
    private String searchGameName;

    public boolean hasAtMostOneNonUserIdField() {
        int count = 0;
        if (viewLatest != null) {
            count++;
        }
        if (viewOnlyWin != null) {
            count++;
        }
        if (viewOnlyLose != null) {
            count++;
        }
        if (searchGameName != null) {
            count++;
        }
        return count <= 1;
    }
}
