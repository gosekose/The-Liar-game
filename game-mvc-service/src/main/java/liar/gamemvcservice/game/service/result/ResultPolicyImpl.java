package liar.gamemvcservice.game.service.result;

import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import liar.gamemvcservice.game.service.dto.PlayerResultInfoDto;
import liar.gamemvcservice.game.service.dto.VotedResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static liar.gamemvcservice.game.domain.GameRole.LIAR;

@Component
@RequiredArgsConstructor
public class ResultPolicyImpl implements ResultPolicy {

    private final JoinPlayerRepository joinPlayerRepository;
    private final GameRepository gameRepository;
    private final VoteRepository voteRepository;

    /**
     * 투표 결과 Liar 올바르게 선택함
     * 라이어 승리 or 시민 승리
     */
    @Override
    public boolean checkWhoWin(Game game, List<VotedResult> votedResults) {
        if (votedResults.size() == 1) {
            if (getGameRole(game.getId(), votedResults) == LIAR) return true;
        }
        return false;
    }

    @Override
    public List<VotedResultDto> getVotedResultDto(Vote vote) {
        return vote.getVotedResults()
                .stream()
                .map(VotedResultDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerResultInfoDto> getPlayersResultInfo(Game game, VotedResult votedResult) {
        return joinPlayerRepository
                .findByGameId(game.getId())
                .stream()
                .map(JoinPlayer::getPlayer)
                .map(player -> new PlayerResultInfoDto(player.getUserId(),
                        player.getGameRole(),
                        votedResult.getUserIds().contains(player.getUserId())))
                .collect(Collectors.toList());
    }


    private GameRole getGameRole(String gameId, List<VotedResult> votedResults) {
        return getJoinPlayer(gameId, votedResults.get(0).getLiarId())
                .getPlayer().getGameRole();
    }

    private JoinPlayer getJoinPlayer(String gameId, String userId) {
        return joinPlayerRepository.findById(gameId + "_" + userId).orElseThrow(NotFoundUserException::new);
    }
}
