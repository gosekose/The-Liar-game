package liar.gamemvcservice.game.service.vote;

import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.Vote;
import liar.gamemvcservice.game.domain.VotedResult;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class VotePolicyImpl implements VotePolicy {

    private final VoteRepository voteRepository;

    /**
     * game의 모든 턴이 끝나면, vote 객체를 생성하여 저장한다.
     */
    @Override
    public String saveVote(Game game) throws InterruptedException {

        Vote findVote = voteRepository.findVoteByGameId(game.getId());
        if (findVote == null) {
            Vote save = voteRepository.save(new Vote(game.getId(), game.getPlayerIds()));
            return save.getId();
        }
        return findVote.getId();
    }

    /**
     * 유저가(userId)가 라이어(liar)를 투표하여
     * Vote 객체 값을 저장한다.
     */
    @Override
    public boolean voteLiarUser(String gameId, String userId, String liarId) {
        Vote vote = voteRepository.findVoteByGameId(gameId);
        LocalDateTime firstModifiedAt = vote.getModifiedAt();

        vote.updateVoteResults(userId, liarId);
        Vote savedVote = voteRepository.save(vote);
        if (!firstModifiedAt.isEqual(savedVote.getModifiedAt())) return true;
        return false;
    }

    /**
     * 가장 많은 LiarId 투표를 받은 결과를 출력한다.
     */
    @Override
    public List<VotedResult> getMostVotedLiarUser(String gameId) {
        Vote vote = voteRepository.findVoteByGameId(gameId);
        return vote.getMostVotedResult();
    }
}
