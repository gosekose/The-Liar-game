package liar.gamemvcservice.game.service.vote;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.VotedResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VotePolicy {

    /**
     * game의 모든 턴이 끝나면, vote 객체를 생성하여 저장한다.
     */
    String saveVote(Game game) throws InterruptedException;

    /**
     * 유저가(userId)가 라이어(liar)를 투표하여
     * Vote 객체 값을 저장한다.
     */
    boolean voteLiarUser(String gameId, String userId, String liarId) throws InterruptedException;

    /**
     * 가장 많은 LiarId 투표를 받은 결과를 출력한다.
     */
    List<VotedResult> getMostVotedLiarUser(String gameId);


}
