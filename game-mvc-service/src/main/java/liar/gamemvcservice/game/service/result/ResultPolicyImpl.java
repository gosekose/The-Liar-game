package liar.gamemvcservice.game.service.result;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import liar.gamemvcservice.game.service.dto.GameResultSaveMessage;
import liar.gamemvcservice.game.service.dto.GameResultToClient;
import liar.gamemvcservice.game.service.dto.PlayersInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static liar.gamemvcservice.game.domain.GameRole.CITIZEN;
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

    /**
     * 게임 결과를 클라이언트에게 알리다.
     */
    @Override
    public GameResultToClient informGameResult(Game game, List<VotedResult> votedResults) {
        GameRole winner = checkWhoWin(game, votedResults) ? CITIZEN : LIAR;
        VotedResult votedResult = getVotedResult(game.getId());

        return GameResultToClient.of(game.getId(), winner,
                joinPlayerRepository.findByGameId(game.getId())
                        .stream()
                        .map(JoinPlayer::getPlayer)
                        .map(player -> new PlayersInfoDto(player.getUserId(), player.getGameRole(),
                                votedResult.getUserIds().contains(player.getUserId())))
                        .collect(Collectors.toList()));

    }

    /**
     * 게임 결과를 result 서버로 메세지 보낸다.
     */
    @Override
    public GameResultSaveMessage messageGameResult(Game game, GameResultToClient gameResult) {
        if (!game.isSendMessage()) {
            updateSendMessage(game);
            return GameResultSaveMessage.of(game, gameResult);
        }
        return null;
    }

    private GameRole getGameRole(String gameId, List<VotedResult> votedResults) {
        return getJoinPlayer(gameId, votedResults.get(0).getLiarId())
                .getPlayer().getGameRole();
    }

    private VotedResult getVotedResult(String gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(NotFoundGameException::new);
        Vote vote = voteRepository.findVoteByGameId(game.getId());

        if (vote == null) throw new NotFoundGameException();

        VotedResult votedResult = vote.getVotedResult(game.getLiarId());
        return votedResult;
    }

    private JoinPlayer getJoinPlayer(String gameId, String userId) {
        return joinPlayerRepository.findById(gameId + "_" + userId).orElseThrow(NotFoundUserException::new);
    }

    private void updateSendMessage(Game game) {
        game.sendMessage();
        gameRepository.save(game);
    }

}
