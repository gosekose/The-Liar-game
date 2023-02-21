package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.GameTurnEndException;
import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.exception.exception.NotUserTurnException;
import liar.gamemvcservice.game.controller.dto.request.VoteLiarRequest;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.GameTurnRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import liar.gamemvcservice.game.service.dto.*;
import liar.gamemvcservice.game.service.vote.VotePolicy;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static liar.gamemvcservice.game.domain.GameRole.CITIZEN;
import static liar.gamemvcservice.game.domain.GameRole.LIAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class GameFacadeServiceImplTest extends ThreadServiceOnlyTest {

    @Autowired
    GameFacadeService gameFacadeService;
    @Autowired
    VotePolicy votePolicy;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private JoinPlayerRepository joinPlayerRepository;
    @Autowired
    private GameTurnRepository gameTurnRepository;
    @Autowired
    private VoteRepository voteRepository;

    private String gameId;
    private Game game;
    private String liarId;
    private String citizenId;

    @BeforeEach
    public void init() throws InterruptedException {
        gameId = gameFacadeService.save(new SetUpGameDto("roomId", "1", "gameName",
                Arrays.asList("1", "2", "3", "4")));
        game = gameRepository.findById(gameId).orElseThrow(NotFoundGameException::new);
        votePolicy.saveVote(game);
        liarId = game.getLiarId();
        citizenId = game.getPlayerIds()
                .stream()
                .filter(player -> !player.equals(liarId))
                .findFirst()
                .orElseThrow(NotFoundUserException::new);
    }

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
        gameTurnRepository.deleteAll();
        joinPlayerRepository.deleteAll();
    }


    @Test
    @DisplayName("dto 요청이 오면, redis에 game, joinPlayer를 저장한 후 gameId를 리턴한다, ")
    public void save_success() throws Exception {
        //given
        Game game = gameRepository.findById(gameId).orElseThrow(NotFoundGameException::new);

        //when
        List<JoinPlayer> joinPlayers = joinPlayerRepository.findByGameId(game.getId());
        List<JoinPlayer> liar = getJoinPlayersByRole(joinPlayers, GameRole.LIAR);
        List<JoinPlayer> citizens = getJoinPlayersByRole(joinPlayers, GameRole.CITIZEN);

        //then
        assertThat(game.getGameName()).isEqualTo("gameName");
        assertThat(game.getHostId()).isEqualTo("1");
        assertThat(game.getRoomId()).isEqualTo("roomId");
        assertThat(game.getTopic()).isNotNull();
        assertThat(game.getLiarId()).isNotNull();
        assertThat(joinPlayers.size()).isEqualTo(4);
        assertThat(liar.size()).isEqualTo(1);
        assertThat(citizens.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("dto 요청이 오면, user의 Role을 체크한다. ")
    public void checkPlayerRole() throws Exception {
        //given
        List<Player> players = getPlayers(gameId);

        //when
        List<Player> citizens = getPlayersByRole(players, GameRole.CITIZEN);
        List<Player> liar = getPlayersByRole(players, GameRole.LIAR);

        //then
        assertThat(players.get(0).getUserId()).isEqualTo("1");
        assertThat(citizens.size()).isEqualTo(3);
        assertThat(liar.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("gameId가 없으면 player의 role을 체크할 수 없고, NotFoundGameException을 발생시킨다.")
    public void checkPlayerRole_fail_notFoundGameId() throws Exception {
        //when
        String wrongId = "wrongId";

        //then
        assertThatThrownBy(() -> {
            gameFacadeService.checkPlayerRole(new CommonDto(wrongId, "1"));
        }).isInstanceOf(NotFoundGameException.class);
    }

    @Test
    @DisplayName("userId가 없으면 player의 role을 체크할 수 없고, NotFoundUserException을 발생시킨다.")
    public void checkPlayerRole_fail_notFoundUserId() throws Exception {
        //when
        String wrongId = "wrongId";

        //then
        assertThatThrownBy(() -> {
                    gameFacadeService.checkPlayerRole(new CommonDto(gameId, wrongId));
                }).isInstanceOf(NotFoundUserException.class);
    }

    @Test
    @DisplayName("gameId와 userId가 올바르면, checkTopic 메소드를 실행해서, topic을 가져온다.")
    public void checkTopic_success() throws Exception {
        //given
        List<Topic> topics = getTopics(gameId);

        //when
        List<Topic> liarTopic = getLiarTopic(topics);
        List<Topic> citizensTopic = getCitizensTopic(topics);

        //then
        assertThat(liarTopic.size()).isEqualTo(1);
        assertThat(citizensTopic.get(0).getTopicName()).isEqualTo(citizensTopic.get(1).getTopicName());
        assertThat(citizensTopic.get(1).getTopicName()).isEqualTo(citizensTopic.get(2).getTopicName());
    }


    @Test
    @DisplayName("gameId가 없다면, topic을 조회할 수 없고, NotFoundGameException이 발생한다.")
    public void checkTopic_fail_notFoundGameId() throws Exception {
        //when
        String wrongId = "wrongId";

        //then
        assertThatThrownBy(() -> {
            gameFacadeService.checkTopic(new CommonDto(wrongId, "1"));
        }).isInstanceOf(NotFoundGameException.class);
    }

    @Test
    @DisplayName("userId가 없다면, topic을 조회할 수 없고, NotFoundUserException이 발생한다.")
    public void checkTopic_fail_notFoundUserId() throws Exception {
        //when
        String wrongId = "wrongId";

        //then
        assertThatThrownBy(() -> {
            gameFacadeService.checkTopic(new CommonDto(gameId, wrongId));
        }).isInstanceOf(NotFoundUserException.class);
    }

    @Test
    @DisplayName("findJoinMemberOfRequestGame")
    public void findJoinMemberOfRequestGame_success() throws Exception {
        //when
        JoinPlayer joinPlayer = gameFacadeService.findJoinPlayer(gameId, "1");

        //then
        assertThat(joinPlayer).isNotNull();
    }

    @Test
    @DisplayName("findJoinMemberOfRequestGame")
    public void findJoinMemberOfRequestGame_exception() throws Exception {
        //when
        String userId = "100";

        //then
        assertThatThrownBy(() -> {
            gameFacadeService.findJoinPlayer(gameId, userId);
        }).isInstanceOf(NotFoundGameException.class);
    }

    @Test
    @DisplayName("단일 스레드 상황에서 턴을 정하는 메서드 테스트")
    public void setUpTurn() throws Exception {
        //given
        List<String> firstRequest = gameFacadeService.setUpTurn(gameId);

        //when
        List<String> secondRequest = gameFacadeService.setUpTurn(gameId);

        //then
        assertThat(firstRequest).isEqualTo(secondRequest);
        assertThat(firstRequest.size()).isEqualTo(4);
        assertThat(firstRequest.get(0)).isNotNull();
    }

    @Test
    @DisplayName("멀티 스레드 상황에서 턴을 정하는 메서드 테스트")
    public void setUpTurn_multiThread() throws Exception {
        //given
        num = 10;
        threads = new Thread[num];
        List<String>[] results = new List[num];

        //when
        for (int i = 0; i < num; i++) {
            int finalIdx = i;
            threads[i] = new Thread(() -> {
                results[finalIdx] = gameFacadeService.setUpTurn(gameId);
            });}
        runThreads();

        //then
        assertThatResultsAllEqual(results);
        assertThat(results[0].size()).isEqualTo(4);
        assertThat(results[0].get(0)).isNotNull();
    }

    @Test
    @DisplayName("플레이어 턴이라면, 턴을 업데이트하고 다음 턴을 리턴한다.")
    public void updateAndInformPlayerTurn() throws Exception {
        //given
        List<String> gamePlayerTurns = gameFacadeService.setUpTurn(gameId);

        //when
        NextTurn nextTurn = gameFacadeService.setNextTurnWhenValidated(gameId, gamePlayerTurns.get(0));

        //then
        assertThat(nextTurn.getUserIdOfNextTurn()).isEqualTo(gamePlayerTurns.get(1));
        assertThat(nextTurn.isLastTurn()).isFalse();
    }

    @Test
    @DisplayName("플레이어 턴이 아니라면, NotUserTurnException 예외 발생")
    public void updateAndInformPlayerTurn_notTurn() throws Exception {
        //given
        List<String> gamePlayerTurns = gameFacadeService.setUpTurn(gameId);

        //when
        String userId = gamePlayerTurns.get(3);

        //then
        assertThatThrownBy(() -> {
            gameFacadeService.setNextTurnWhenValidated(gameId, userId);
        }).isInstanceOf(NotUserTurnException.class);
    }

    @Test
    @DisplayName("플레이어가 마지막 턴이라면, NextTurn null 반환하고 vote를 set한다.")
    public void updateAndInformPlayerTurn_lastTurn() throws Exception {
        //given
        List<String> gamePlayerTurns = gameFacadeService.setUpTurn(gameId);

        //when
        NextTurn nextTurn = playUntilLastTurn(gamePlayerTurns);
        Vote vote = voteRepository.findVoteByGameId(gameId);

        //then
        assertThat(nextTurn.getUserIdOfNextTurn()).isNull();
        assertThat(nextTurn.isLastTurn()).isTrue();
        assertThat(vote.getVotedResults().size()).isEqualTo(gamePlayerTurns.size());
    }

    @Test
    @DisplayName("플레이어가 마지막 턴이 지났음에도 요청이 오면, GameTurnEndException 반환")
    public void updateAndInformPlayerTurn_exception() throws Exception {
        //given
        List<String> gamePlayerTurns = gameFacadeService.setUpTurn(gameId);
        playUntilLastTurn(gamePlayerTurns);

        //then
        assertThatThrownBy(() -> {
            gameFacadeService
                    .setNextTurnWhenValidated(gameId, gamePlayerTurns.get(0));
        }).isInstanceOf(GameTurnEndException.class);
    }

    @Test
    @DisplayName("voteLiarUser후 값이 변하면 true를 리턴한다.")
    public void voteLiarUser_returnTrue() throws Exception {
        //given
        doPlayUntilLastTurns();

        //when
        boolean result1 = gameFacadeService.voteLiarUser(new VoteLiarRequest(gameId, "1", "2"));
        boolean result2 = gameFacadeService.voteLiarUser(new VoteLiarRequest(gameId, "2", "3"));
        boolean result3 = gameFacadeService.voteLiarUser(new VoteLiarRequest(gameId, "3", "2"));
        List<VotedResult> mostVotedResult = voteRepository.findVoteByGameId(gameId).getMostVotedResult();

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isTrue();
        assertThat(mostVotedResult.size()).isEqualTo(1);
        assertThat(mostVotedResult.get(0).getLiarId()).isEqualTo("2");
    }

    @Test
    @DisplayName("voteLiarUser후 없는 liar 요청이 온 경우, false를 리턴한다.")
    public void voteLiarUser_returnFalse() throws Exception {
        //given
        doPlayUntilLastTurns();

        //when
        boolean result1 = gameFacadeService.voteLiarUser(new VoteLiarRequest(gameId, "1", "7"));
        boolean result2 = gameFacadeService.voteLiarUser(new VoteLiarRequest(gameId, "2", "8"));
        boolean result3 = gameFacadeService.voteLiarUser(new VoteLiarRequest(gameId, "3", "9"));
        List<VotedResult> mostVotedResult = voteRepository.findVoteByGameId(gameId).getMostVotedResult();

        //then
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
        assertThat(mostVotedResult.get(0).getCnt()).isEqualTo(0);
    }

    @Test
    @DisplayName("멀티 스레드 환경에서 voteLiarUser후 값이 변하면 true를 리턴한다.")
    public void voteLiarUser_multiThread() throws Exception {
        num = 4;
        threads = new Thread[num];
        doPlayUntilLastTurns();
        boolean[] results = new boolean[num];

        //when
        for (int i = 0; i < num; i++) {
            int finalIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    results[finalIndex] = gameFacadeService.voteLiarUser(new VoteLiarRequest(gameId, String.valueOf(finalIndex + 1), "2"));
                    System.out.println("finalIndex = " + finalIndex);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        runThreads();

        List<VotedResult> mostVotedResult = voteRepository.findVoteByGameId(gameId).getMostVotedResult();

        //then

        assertThatAllVoteLiarUserReturnTure(results);
        assertThat(mostVotedResult.size()).isEqualTo(1);
        assertThat(mostVotedResult.get(0).getCnt()).isEqualTo(4);
        assertThat(mostVotedResult.get(0).getUserIds().size()).isEqualTo(4);
        assertThat(mostVotedResult.get(0).getLiarId()).isEqualTo("2");
    }


    @Test
    @DisplayName("라이어가 이긴 결과를 모든 클라이언트에게 informGameResult로 알린다.")
    public void messageGameResultToClient() throws Exception {
        //given
        voteMostOthers();

        //when
        GameResultToClientDto gameResult = gameFacadeService
                .sendGameResultToClient(gameId);

        List<PlayerResultInfo> playersInfo = gameResult.getPlayersInfo();

        //then
        assertThat(gameResult.getGameId()).isEqualTo(game.getId());
        assertThat(gameResult.getWinner()).isEqualTo(LIAR);
        assertThat(playersInfo.size()).isEqualTo(4);
        assertThat(playersInfo.get(0).getAnswers()).isFalse();
    }

    @Test
    @DisplayName("라이어가 이긴 결과를 모든 클라이언트에게 informGameResult로 알린다.")
    public void informGameResult_winLiar2() throws Exception {
        //given
        voteLiarAndOthersSame();

        //when
        GameResultToClientDto gameResult = gameFacadeService.sendGameResultToClient(gameId);

        List<PlayerResultInfo> playersInfo = gameResult.getPlayersInfo();

        //then
        assertThat(gameResult.getGameId()).isEqualTo(game.getId());
        assertThat(gameResult.getWinner()).isEqualTo(LIAR);
        assertThat(playersInfo.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("게임 결과에 대한 메세지를 전송한다.")
    public void messageGameResult() throws Exception {
        //given
        voteMostLiar();
        GameResultToServerDto message = gameFacadeService.sendGameResultToServer(gameId);

        //then
        assertThat(message.getGameId()).isEqualTo(game.getId());
        assertThat(message.getWinner()).isEqualTo(CITIZEN);
        assertThat(message.getGameName()).isEqualTo(game.getGameName());
        assertThat(message.getHostId()).isEqualTo(game.getHostId());
        assertThat(message.getRoomId()).isEqualTo(game.getRoomId());
        assertThat(message.getTopicId()).isEqualTo(game.getTopic().getId());
        assertThat(message.getTotalUserCnt()).isEqualTo(game.getPlayerIds().size());
    }

    @Test
    @DisplayName("멀티 스레드 환경에서 게임 결과에 대한 메세지를 전송하되, 단 한번만 발송하고 그 이후에는 null을 리턴한다.")
    public void messageGameResult_multiThead() throws Exception {
        //given
        int result = 0;
        GameResultToServerDto message = null;
        GameResultToServerDto[] messages = initGameResultSateMessage();
        voteMostLiar();

        //when
        for (int i = 0; i < num; i++) {
            int finalIdx = i;
            threads[i] = new Thread(() -> {
                messages[finalIdx] = gameFacadeService.sendGameResultToServer(gameId);
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
    }

    private GameResultToClientDto getGameResultToClient() throws InterruptedException {
        voteMostLiar();
        GameResultToClientDto gameResult = gameFacadeService.sendGameResultToClient(gameId);
        return gameResult;
    }

    @NotNull
    private GameResultToServerDto[] initGameResultSateMessage() {
        num = 100;
        threads = new Thread[num];
        GameResultToServerDto[] messages = new GameResultToServerDto[num];
        Arrays.fill(messages,new GameResultToServerDto());
        return messages;
    }


    private void voteMostLiar() throws InterruptedException {
        for (int i = 1; i <= game.getPlayerIds().size(); i++)
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i), liarId);
    }

    private void voteMostOthers() throws InterruptedException {
        for (int i = 1; i <= game.getPlayerIds().size(); i++)
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i), citizenId);
    }

    private void voteLiarAndOthersSame() throws InterruptedException {
        for (int i = 1; i <= game.getPlayerIds().size() / 2; i++)
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i), liarId);
        for (int i = (game.getPlayerIds().size() / 2) + 1; i <= game.getPlayerIds().size(); i++)
            votePolicy.voteLiarUser(game.getId(), String.valueOf(i), citizenId);
    }

    @NotNull
    private static List<JoinPlayer> getJoinPlayersByRole(List<JoinPlayer> joinPlayers, GameRole liar) {
        return joinPlayers.stream()
                .filter(player -> player.getPlayer().getGameRole() == liar)
                .collect(Collectors.toList());
    }

    @NotNull
    private static List<Player> getPlayersByRole(List<Player> players, GameRole citizen) {
        return players.stream()
                .filter(player -> player.getGameRole() == citizen)
                .collect(Collectors.toList());
    }

    @NotNull
    private List<Topic> getTopics(String gameId) {
        List<Topic> topics = new ArrayList<>();
        for (int i = 1; i < 5; i++) topics.add(gameFacadeService.checkTopic(new CommonDto(gameId, String.valueOf(i))));
        return topics;
    }

    @NotNull
    private static List<Topic> getCitizensTopic(List<Topic> topics) {
        return topics.stream()
                .filter(topic -> topic != null)
                .collect(Collectors.toList());
    }

    @NotNull
    private static List<Topic> getLiarTopic(List<Topic> topics) {
        return topics.stream()
                .filter(topic -> topic == null)
                .collect(Collectors.toList());
    }

    private List<Player> getPlayers(String gameId) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i < 5; i++)
            players.add(gameFacadeService.checkPlayerRole(new CommonDto(gameId, String.valueOf(i))));
        return players;
    }

    private void assertThatResultsAllEqual(List<String>[] results) {
        for (int i = 0; i < num; i++) {
            if (i < num - 1) assertThat(results[i]).isEqualTo(results[i + 1]);
        }
    }

    private NextTurn playUntilLastTurn(List<String> gamePlayerTurns) throws InterruptedException {
        NextTurn nextTurn = null;
        for (int i = 0; i < gamePlayerTurns.size() * 2; i++) {
            nextTurn = gameFacadeService.setNextTurnWhenValidated(gameId,
                    gamePlayerTurns.get(i % gamePlayerTurns.size()));
        }
        return nextTurn;
    }


    private void doPlayUntilLastTurns() throws InterruptedException {
        List<String> gamePlayerTurns = gameFacadeService.setUpTurn(gameId);
        playUntilLastTurn(gamePlayerTurns);
    }

    private void assertThatAllVoteLiarUserReturnTure(boolean[] results) {
        for (int i = 0; i < num; i++) {
            assertThat(results[i]).isTrue();
        }
    }
}