package liar.gamemvcservice.game.domain;

import jakarta.persistence.Id;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@RedisHash(value = "Vote")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

    @Id
    private String id;
    @Indexed
    private String gameId;
    private List<VotedResult> votedResults;
    private LocalDateTime modifiedAt;

    public Vote(String gameId, List<String> userIds) {
        this.id = UUID.randomUUID().toString();
        this.gameId = gameId;
        this.votedResults = userIds
                .stream()
                .map(userId -> new VotedResult(userId, 0))
                .collect(Collectors.toList());
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateVoteResults(String userId, String liarId) {
        votedResults.stream()
                .filter(vote -> vote.getLiarId().equals(liarId))
                .findFirst()
                .ifPresent(votedResult -> {
                    votedResult.addUserId(userId);
                    modifiedAt = LocalDateTime.now();
                });

    }

    public List<VotedResult> getMostVotedResult() {
        return votedResults.stream()
                .collect(Collectors.groupingBy(VotedResult::getCnt))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList());
    }

    public VotedResult getVotedResult(String userId) {
        return votedResults.stream()
                .filter(vote -> vote.getLiarId().equals(userId))
                .findFirst()
                .orElseThrow(NotFoundUserException::new);
    }
    
    @Override
    public String toString() {
        return "Vote:" + id;
    }
}
