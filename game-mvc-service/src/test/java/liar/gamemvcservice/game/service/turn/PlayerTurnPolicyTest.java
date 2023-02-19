package liar.gamemvcservice.game.service.turn;

import liar.gamemvcservice.exception.exception.NotUserTurnException;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameTurn;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.GameTurnRepository;
import liar.gamemvcservice.game.service.ThreadServiceOnlyTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PlayerTurnPolicyTest extends ThreadServiceOnlyTest {

    @Autowired
    PlayerTurnPolicy playerTurnPolicy;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameTurnRepository gameTurnRepository;

    private Game game;

    @BeforeEach
    public void init() {
        setUpThead();
        game = gameRepository.save(Game.of(new SetUpGameDto("1", "1", "1",
                Arrays.asList("1", "2", "3", "4", "5"))));
    }

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
        gameTurnRepository.deleteAll();
    }

    @Test
    @DisplayName("게임이 시작되면, 1회성 플레이어의 턴을 정하는 setUpTurn 요청을 보낸다.")
    public void setUpTurn() throws Exception {
        //given
        GameTurn gameTurn = playerTurnPolicy.setUpTurn(game);

        //then
        assertThat(gameTurn.getGameId()).isEqualTo(game.getId());
        assertThat(gameTurn.getNowTurn()).isEqualTo(0);
        assertThat(gameTurn.getPlayerTurnsConsistingOfUserId().size()).isEqualTo(5);
    }
    
    @Test
    @DisplayName("게임이 시작되면, 1회성 플레이어의 턴을 정하는 setUpTurn 요청을 보낸다.")
    public void setUpTurn_multiThread() throws Exception {
        //given
        List<GameTurn> gameTurns = new ArrayList<>();

        //when
        for (int i = 0; i < num; i++) {
            threads[i] = new Thread(() -> {
                gameTurns.add(playerTurnPolicy.setUpTurn(game));
            });
        }

        runThreads();

        //then
        assertThatGameTurnsAllEqual(gameTurns);

    }

    @Test
    @DisplayName("플레이어의 턴이 확인이 되면 플레이어의 턴을 업데이트 한다.")
    public void updatePlayerTurnWhenPlayerTurnIsValidated() throws Exception {
        //given
        GameTurn gameTurn = playerTurnPolicy.setUpTurn(game);
        List<String> turns = gameTurn.getPlayerTurnsConsistingOfUserId();
        List<GameTurn> results = new ArrayList<>();

        //when
        for (int i = 0; i < turns.size(); i++) {
            GameTurn findGameTurn = gameTurnRepository.findGameTurnByGameId(game.getId()); // gameTurn 업테이트
            results.add(playerTurnPolicy.updateTurnWhenPlayerTurnIsValidated(findGameTurn, turns.get(i)));
        }

        //then
        for (int i = 0; i < turns.size(); i++) {
            assertThat(results.get(i).getNowTurn()).isEqualTo(gameTurn.getNowTurn() + (i + 1));
            assertThat(results.get(i).getGameId()).isEqualTo(game.getId());
        }
    }

    @Test
    @DisplayName("플레이어의 턴이 확인이 되면, 플레이어의 턴을 하되 멀티 스레드 환경에서 원자성이 유지되어야 한다.")
    public void updatePlayerTurnWhenPlayerTurnIsValidated_multiThead() throws Exception {
        //given
        num = 100;
        threads = new Thread[num];
        List<String> firstUserIdsOfGame = new ArrayList<>();
        List<String> gameIds = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            Game game = gameRepository.save(Game.of(new SetUpGameDto(String.valueOf(i + 100), "1", "1",
                    Arrays.asList("1", "2", "3", "4", "5"))));
            gameIds.add(game.getId());
            firstUserIdsOfGame.add(playerTurnPolicy.setUpTurn(game)
                    .getPlayerTurnsConsistingOfUserId().get(0));
        }

        //when
        for (int i = 0; i < num; i++) {
            int finalIdx = i;
            GameTurn gameTurn = gameTurnRepository.findGameTurnByGameId(gameIds.get(finalIdx));
            threads[i] = new Thread(() -> {
                playerTurnPolicy.updateTurnWhenPlayerTurnIsValidated(gameTurn, firstUserIdsOfGame.get(finalIdx));
            });
        }
        runThreads();

        //then
        assertThatFindGameTurnByGameIdAllEqual(gameIds);
    }


    @Test
    @DisplayName("플레이어의 턴이 아니라면 예외를 발생시킨다.")
    public void updatePlayerTurnWhenPlayerTurnIsValidated_exception() throws Exception {
        //given
        GameTurn gameTurn = playerTurnPolicy.setUpTurn(game);
        List<String> turns = gameTurn.getPlayerTurnsConsistingOfUserId();

        //when
        for (int i = 0; i < turns.size() - 1; i++) {
            GameTurn findGameTurn = gameTurnRepository.findGameTurnByGameId(game.getId()); // gameTurn 업테이트
            gameTurn = playerTurnPolicy.updateTurnWhenPlayerTurnIsValidated(findGameTurn, turns.get(i));
        }

        //then
        final GameTurn lastGameTurn = gameTurn;
        Assertions.assertThatThrownBy(() -> {
            playerTurnPolicy.updateTurnWhenPlayerTurnIsValidated(lastGameTurn, turns.get(0));
        }).isInstanceOf(NotUserTurnException.class);
    }


    private void assertThatFindGameTurnByGameIdAllEqual(List<String> gameIds) {
        for (int i = 0; i < num; i++) {
            assertThat(gameTurnRepository.findGameTurnByGameId(gameIds.get(i))
                    .getNowTurn()).isEqualTo(1);
        }
    }

    private void assertThatGameTurnsAllEqual(List<GameTurn> gameTurns) {
        for (int i = 0; i < num; i++) {
            if (i < num - 1) assertThat(gameTurns.get(i).getPlayerTurnsConsistingOfUserId())
                    .isEqualTo(gameTurns.get(i + 1).getPlayerTurnsConsistingOfUserId());
            else assertThat(gameTurns.get(i).getPlayerTurnsConsistingOfUserId())
                    .isEqualTo(gameTurns.get(0).getPlayerTurnsConsistingOfUserId());
        }
    }
}