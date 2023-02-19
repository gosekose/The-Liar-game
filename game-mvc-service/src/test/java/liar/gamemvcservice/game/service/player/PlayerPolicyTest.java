package liar.gamemvcservice.game.service.player;

import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.GameRole;
import liar.gamemvcservice.game.domain.JoinPlayer;
import liar.gamemvcservice.game.domain.Player;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PlayerPolicyTest {

    @Autowired
    PlayerPolicy playerPolicy;

    @Autowired
    JoinPlayerRepository joinPlayerRepository;

    @AfterEach
    public void tearDown() {
        joinPlayerRepository.deleteAll();
    }

    @Test
    @DisplayName("game 객체를 받아, role을 정의한 후 저장한다. 이때 반드시, 다수의 CITIZEN과 한명의 LIAR가 존재 해야한다.")
    public void setUpPlayerRole_success() throws Exception {
        //given
        Game game = Game.of(new SetUpGameDto("1", "1", "1", Arrays.asList("1", "2", "3", "4", "5", "6")));

        //when
        playerPolicy.setUpPlayerRole(game);
        List<JoinPlayer> joinPlayers = joinPlayerRepository.findByGameId(game.getId());

        List<JoinPlayer> liar = joinPlayers.stream()
                .filter(f -> f.getPlayer().getGameRole() == GameRole.LIAR)
                .collect(Collectors.toList());

        List<JoinPlayer> citizens = joinPlayers.stream()
                .filter(f -> f.getPlayer().getGameRole() == GameRole.CITIZEN)
                .collect(Collectors.toList());

        //then

        assertThat(liar.size()).isEqualTo(1);
        assertThat(citizens.size()).isEqualTo(5);
        assertThat(citizens.get(0).getPlayer().getUserId()).isNotEqualTo(citizens.get(1).getPlayer().getUserId());
        assertThat(citizens.get(1).getPlayer().getUserId()).isNotEqualTo(citizens.get(2).getPlayer().getUserId());
        assertThat(citizens.get(2).getPlayer().getUserId()).isNotEqualTo(citizens.get(3).getPlayer().getUserId());
        assertThat(citizens.get(3).getPlayer().getUserId()).isNotEqualTo(citizens.get(4).getPlayer().getUserId());
        assertThat(citizens.get(4).getPlayer().getUserId()).isNotEqualTo(citizens.get(1).getPlayer().getUserId());

        assertThat(liar.get(0).getPlayer().getGameRole()).isEqualTo(GameRole.LIAR);
    }

    @Test
    @DisplayName("player의 game내 role 정보를 제공한다. ")
    public void checkPlayerInfo_success() throws Exception {
        //given
        Game game = Game.of(new SetUpGameDto("1", "1", "1", Arrays.asList("1", "2", "3", "4", "5", "6")));
        playerPolicy.setUpPlayerRole(game);

        //when
        Player player1 = playerPolicy.checkPlayerInfo(game.getId(), "1");
        Player player2 = playerPolicy.checkPlayerInfo(game.getId(), "2");
        Player player3 = playerPolicy.checkPlayerInfo(game.getId(), "3");
        Player player4 = playerPolicy.checkPlayerInfo(game.getId(), "4");
        Player player5 = playerPolicy.checkPlayerInfo(game.getId(), "5");
        Player player6 = playerPolicy.checkPlayerInfo(game.getId(), "6");

        List<Player> players = Arrays.asList(player1, player2, player3, player4, player5, player6);

        List<Player> liars = players.stream().filter(player -> player.getGameRole() == GameRole.LIAR)
                .collect(Collectors.toList());

        //then
        assertThat(player1.getUserId()).isEqualTo("1");
        assertThat(player2.getUserId()).isEqualTo("2");
        assertThat(player3.getUserId()).isEqualTo("3");
        assertThat(player4.getUserId()).isEqualTo("4");
        assertThat(player5.getUserId()).isEqualTo("5");
        assertThat(player6.getUserId()).isEqualTo("6");

        assertThat(liars.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("player의 userId가 존재하지 않는다면, NotFoundUserException")
    public void checkPlayerInfo_notFoundUserException() throws Exception {
        //given
        Game game = Game.of(new SetUpGameDto("1", "1", "1",
                Arrays.asList("1", "2", "3", "4", "5", "6")));

        //when
        playerPolicy.setUpPlayerRole(game);

        //then
        Assertions.assertThatThrownBy( () -> {
            playerPolicy.checkPlayerInfo(game.getId(), "10");
        })
                .isInstanceOf(NotFoundUserException.class);

    }
}