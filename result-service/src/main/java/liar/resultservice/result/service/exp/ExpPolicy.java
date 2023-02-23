package liar.resultservice.result.service.exp;

import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Level;
import org.springframework.stereotype.Component;

@Component
public interface ExpPolicy {

    /**
     * 게임 결과에 따라 exp를 계산한다.
     * @return long
     */
    Long calculateExp(GameRole role, boolean win, boolean answer, int totalGameMember);

    /**
     * exp를 받아 level을 구성한다
     * @return level;
     */
    Level nextLevel(long exp);

    boolean isBiggerLevelExp(Level level, long exp);


}
