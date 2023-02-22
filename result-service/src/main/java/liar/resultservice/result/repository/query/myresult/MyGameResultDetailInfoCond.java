package liar.resultservice.result.repository.query.myresult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyGameResultDetailInfoCond {

    private Boolean latest;
    private Boolean wins;
    private Boolean loes;
    private String gameName;
}
