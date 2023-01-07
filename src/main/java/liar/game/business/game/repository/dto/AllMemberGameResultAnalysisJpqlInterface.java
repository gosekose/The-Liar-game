package liar.game.business.game.repository.dto;

import java.time.LocalDateTime;

public interface AllMemberGameResultAnalysisJpqlInterface {

    Long getId();
    Long getWin();
    Long getLose();
    Double getRate();
    LocalDateTime getLastJoinGameTime();

}
