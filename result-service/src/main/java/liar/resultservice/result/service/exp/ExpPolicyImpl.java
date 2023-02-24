package liar.resultservice.result.service.exp;

import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static liar.resultservice.result.domain.GameRole.CITIZEN;
import static liar.resultservice.result.domain.GameRole.LIAR;

@Component
public class ExpPolicyImpl implements ExpPolicy {

    @Value("${game.member.limit.min}")
    private int limitMin;

    @Value("${game.member.limit.max}")
    private int limitMax;

    /**
     * 게임 결과에 따라 exp를 계산한다.
     *
     * @param role
     * @param win
     * @param totalGameMember
     * @return long
     */
    @Override
    public Long calculateExp(GameRole role, boolean win, boolean answer, int totalGameMember) {
        long baseExp = 10L;

        if (role == LIAR) {
            baseExp += win ? 40 - (totalGameMember - limitMin) : 5;
        }
        else if (role == CITIZEN) {
            baseExp += win ?(answer ? 30 - (limitMax - totalGameMember) : 10) : (answer ? 3 : 0);
        }
        return baseExp;
    }

    /**
     * exp를 받아 level을 구성한다
     *
     * @return level;
     */
    @Override
    public Level nextLevel(long exp) {
        for (Level level : Level.values()) {
            if (exp < level.getMaxExp()) {
                return level;
            }
        }
        return Level.LIAR;
    }

    @Override
    public boolean isBiggerLevelExp(Level level, long exp) {
        return level.getMaxExp() <= exp ? true : false;
    }
}
