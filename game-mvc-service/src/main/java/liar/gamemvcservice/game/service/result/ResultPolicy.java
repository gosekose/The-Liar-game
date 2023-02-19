package liar.gamemvcservice.game.service.result;

import org.springframework.stereotype.Component;

@Component
public interface ResultPolicy {

    /**
     * 투표 결과 Liar 올바르게 선택함
     * 라이어 승리 or 시민 승리
     */
    void checkWhoWin();

    /**
     * liar가 승리했는지 혹은 liar가 승리했는지 저장하다.
     */
    void saveWhoWin();

    /**
     * 승리 결과를 알리다.
     */
    void informWhoWin();

}
