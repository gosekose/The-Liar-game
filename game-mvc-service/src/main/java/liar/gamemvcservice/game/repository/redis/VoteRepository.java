package liar.gamemvcservice.game.repository.redis;

import liar.gamemvcservice.game.domain.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, String> {
    Vote findVoteByGameId(String gameId);
}
