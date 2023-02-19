package liar.gamemvcservice.game.service.result;

import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import liar.gamemvcservice.game.service.ThreadServiceOnlyTest;
import liar.gamemvcservice.game.service.dto.GameResultSaveMessage;
import liar.gamemvcservice.game.service.dto.GameResultToClient;
import liar.gamemvcservice.game.service.dto.PlayersInfoDto;
import liar.gamemvcservice.game.service.player.PlayerPolicy;
import liar.gamemvcservice.game.service.topic.TopicPolicy;
import liar.gamemvcservice.game.service.vote.VotePolicy;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static liar.gamemvcservice.game.domain.GameRole.CITIZEN;
import static liar.gamemvcservice.game.domain.GameRole.LIAR;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ResultPolicyTest extends ThreadServiceOnlyTest {

    @Autowired
    ResultPolicy resultPolicy;
    @Autowired
    PlayerPolicy playerPolicy;
    @Autowired
    VotePolicy votePolicy;
    @Autowired
    TopicPolicy topicPolicy;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    JoinPlayerRepository joinPlayerRepository;
    @Autowired
    VoteRepository voteRepository;

    private Game game;
    private String liarId;
    private String citizenId;

    @BeforeEach
    public void init() throws InterruptedException {
        game = gameRepository.save(Game.of(new SetUpGameDto("1", "1", "1",
                Arrays.asList("1", "2", "3", "4", "5", "6"))));
        liarId = playerPolicy.setUpPlayerRole(game);
        game = game.updateTopicOfGame(topicPolicy.setUp(), liarId);
        gameRepository.save(game);
        votePolicy.saveVote(game);
        citizenId = game.getPlayerIds()
                .stream()
                .filter(player -> !player.equals(liarId))
                .findFirst()
                .orElseThrow(NotFoundUserException::new);
    }

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
        joinPlayerRepository.deleteAll();
        voteRepository.deleteAll();
    }

    @Test
    @DisplayName("checkWhoWin: 라이어가 가장 많은 득표수를 얻었을 경우 win은 true를 출력")
    public void checkWhoWin_mostLiar() throws Exception {
        //given
        voteMostLiar();

        //when
        boolean win = resultPolicy.checkWhoWin(game, votePolicy.getMostVotedLiarUser(game.getId()));

        //then
        assertThat(win).isTrue();
    }

    @Test
    @DisplayName("checkWhoWin: 다른 유저가 가장 많은 득표수를 얻었을 경우 win은 false를 출력")
    public void checkWhoWin_mostOthers() throws Exception {
        //given
        voteMostOthers();

        //when
        boolean win = resultPolicy.checkWhoWin(game, votePolicy.getMostVotedLiarUser(game.getId()));

        //then
        assertThat(win).isFalse();
    }

    @Test
    @DisplayName("checkWhoWin: 라이어와 다른 유저가 가장 많은 득표수를 얻었을 경우 win은 false를 출력")
    public void checkWhoWin_mostLiarAndOthers() throws Exception {
        //given
        voteLiarAndOthersSame();

        //when
        boolean win = resultPolicy.checkWhoWin(game, votePolicy.getMostVotedLiarUser(game.getId()));

        //then
        assertThat(win).isFalse();
    }

    @Test
    @DisplayName("시민이 이긴 결과를 모든 클라이언트에게 informGameResult로 알린다.")
    public void informGameResult_winCitizen() throws Exception {
        //given
        GameResultToClient gameResult = getGameResultToClient();

        List<PlayersInfoDto> playersInfo = gameResult.getPlayersInfo();

        //then
        assertThat(gameResult.getGameId()).isEqualTo(game.getId());
        assertThat(gameResult.getWinner()).isEqualTo(CITIZEN);
        assertThat(playersInfo.size()).isEqualTo(6);
        assertThat(playersInfo.get(0).getAnswers()).isTrue();
    }

    @Test
    @DisplayName("라이어가 이긴 결과를 모든 클라이언트에게 informGameResult로 알린다.")
    public void informGameResult_winLiar() throws Exception {
        //given
        voteMostOthers();

        //when
        GameResultToClient gameResult = resultPolicy
                .informGameResult(game, votePolicy.getMostVotedLiarUser(game.getId()));

        List<PlayersInfoDto> playersInfo = gameResult.getPlayersInfo();

        //then
        assertThat(gameResult.getGameId()).isEqualTo(game.getId());
        assertThat(gameResult.getWinner()).isEqualTo(LIAR);
        assertThat(playersInfo.size()).isEqualTo(6);
        assertThat(playersInfo.get(0).getAnswers()).isFalse();
    }

    @Test
    @DisplayName("라이어가 이긴 결과를 모든 클라이언트에게 informGameResult로 알린다.")
    public void informGameResult_winLiar2() throws Exception {
        //given
        voteLiarAndOthersSame();

        //when
        GameResultToClient gameResult = resultPolicy
                .informGameResult(game, votePolicy.getMostVotedLiarUser(game.getId()));

        List<PlayersInfoDto> playersInfo = gameResult.getPlayersInfo();

        //then
        assertThat(gameResult.getGameId()).isEqualTo(game.getId());
        assertThat(gameResult.getWinner()).isEqualTo(LIAR);
        assertThat(playersInfo.size()).isEqualTo(6);
        assertThat(playersInfo.get(0).getAnswers()).isFalse();
    }

    @Test
    @DisplayName("게임 결과에 대한 메세지를 전송한다.")
    public void messageGameResult() throws Exception {
        //given
        GameResultToClient gameResult = getGameResultToClient();

        GameResultSaveMessage message = resultPolicy.messageGameResult(game, gameResult);

        //then
        assertThat(message.getGameId()).isEqualTo(game.getId());
        assertThat(message.getWinner()).isEqualTo(CITIZEN);
        assertThat(message.getGameName()).isEqualTo(game.getGameName());
        assertThat(message.getHostId()).isEqualTo(game.getHostId());
        assertThat(message.getRoomId()).isEqualTo(game.getRoomId());
        assertThat(message.getTopicId()).isEqualTo(game.getTopic().getId());
        assertThat(message.getTotalUserCnt()).isEqualTo(game.getPlayerIds().size());
        assertThat(message.getPlayersInfo()).isEqualTo(gameResult.getPlayersInfo());
    }

    @Test
    @DisplayName("멀티 스레드 환경에서 게임 결과에 대한 메세지를 전송하되, 단 한번만 발송하고 그 이후에는 null을 리턴한다.")
    public void messageGameResult_multiThead() throws Exception {
        //given
        int result = 0;
        GameResultSaveMessage message = null;

        GameResultSaveMessage[] messages = initGameResultSateMessage();
        GameResultToClient gameResult = getGameResultToClient();

        //when
        for (int i = 0; i < num; i++) {
            int finalIdx = i;
            threads[i] = new Thread(() -> {
                messages[finalIdx] = resultPolicy.messageGameResult(game, gameResult);
            });
        }

        runThreads();

        for (int i = 0; i < num; i++) {
            if (messages[i] != null)  {
                result++;
                message = messages[i];
            }
        }

        //then
        assertThat(result).isEqualTo(1);
        assertThat(message.getGameId()).isEqualTo(game.getId());
        assertThat(message.getWinner()).isEqualTo(CITIZEN);
        assertThat(message.getGameName()).isEqualTo(game.getGameName());
        assertThat(message.getHostId()).isEqualTo(game.getHostId());
        assertThat(message.getRoomId()).isEqualTo(game.getRoomId());
        assertThat(message.getTopicId()).isEqualTo(game.getTopic().getId());
        assertThat(message.getTotalUserCnt()).isEqualTo(game.getPlayerIds().size());
        assertThat(message.getPlayersInfo()).isEqualTo(gameResult.getPlayersInfo());
    }

    private GameResultToClient getGameResultToClient() throws InterruptedException {
        voteMostLiar();
        GameResultToClient gameResult = resultPolicy
                .informGameResult(game, votePolicy.getMostVotedLiarUser(game.getId()));
        return gameResult;
    }

    @NotNull
    private GameResultSaveMessage[] initGameResultSateMessage() {
        num = 100;
        threads = new Thread[num];
        GameResultSaveMessage[] messages = new GameResultSaveMessage[num];
        Arrays.fill(messages,new GameResultSaveMessage());
        return messages;
    }


    private void voteMostLiar() throws InterruptedException {
        for (int i = 0; i < game.getPlayerIds().size(); i++)
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i), liarId);
    }

    private void voteMostOthers() throws InterruptedException {
        for (int i = 0; i < game.getPlayerIds().size(); i++)
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i), citizenId);
    }

    private void voteLiarAndOthersSame() throws InterruptedException {
        for (int i = 0; i < game.getPlayerIds().size() / 2; i++)
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i), liarId);
        for (int i = game.getPlayerIds().size() / 2; i < game.getPlayerIds().size(); i++)
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i), citizenId);
    }
}