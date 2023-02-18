package liar.gamemvcservice.game.service.turn;

import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameTurn;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.GameTurnRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import liar.gamemvcservice.game.service.ThreadServiceOnlyTest;
import liar.gamemvcservice.game.service.player.PlayerPolicyImpl;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        for (int i = 0; i < num; i++) {
            if (i < num - 1) assertThat(gameTurns.get(i).getPlayerTurnsConsistingOfUserId())
                    .isEqualTo(gameTurns.get(i + 1).getPlayerTurnsConsistingOfUserId());
            else assertThat(gameTurns.get(i).getPlayerTurnsConsistingOfUserId())
                    .isEqualTo(gameTurns.get(0).getPlayerTurnsConsistingOfUserId());
        }

    }

}