package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.controller.dto.GameUserInfoDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.GameRepository;
import liar.gamemvcservice.game.repository.JoinPlayerRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GameServiceTest {

    @Autowired
    GameService gameService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private JoinPlayerRepository joinPlayerRepository;

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
        joinPlayerRepository.deleteAll();
    }


    @Test
    @DisplayName("dto 요청이 오면, redis에 game, joinPlayer를 저장한 후 gameId를 리턴한다, ")
    public void save_success() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));

        //when
        String gameId = gameService.save(setUpGameDto);
        Game game = gameRepository.findById(gameId).orElseThrow(NotFoundGameException::new);
        List<JoinPlayer> joinPlayers = joinPlayerRepository.findByGameId(game.getId());

        List<JoinPlayer> liar = joinPlayers.stream()
                .filter(player -> player.getPlayer().getGameRole() == GameRole.LIAR)
                .collect(Collectors.toList());

        List<JoinPlayer> citizens = joinPlayers.stream()
                .filter(player -> player.getPlayer().getGameRole() == GameRole.CITIZEN)
                .collect(Collectors.toList());


        //then
        assertThat(game.getGameName()).isEqualTo("gameName");
        assertThat(game.getHostId()).isEqualTo("1");
        assertThat(game.getRoomId()).isEqualTo("roomId");
        assertThat(game.getTopic()).isNotNull();

        assertThat(joinPlayers.size()).isEqualTo(4);
        assertThat(liar.size()).isEqualTo(1);
        assertThat(citizens.size()).isEqualTo(3);

    }


    @Test
    @DisplayName("dto 요청이 오면, user의 Role을 체크한다. ")
    public void checkPlayerRole() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));
        String gameId = gameService.save(setUpGameDto);

        //when
        Player player1 = gameService.checkPlayerRole(new GameUserInfoDto(gameId, "1"));
        Player player2 = gameService.checkPlayerRole(new GameUserInfoDto(gameId, "2"));
        Player player3 = gameService.checkPlayerRole(new GameUserInfoDto(gameId, "3"));
        Player player4 = gameService.checkPlayerRole(new GameUserInfoDto(gameId, "4"));

        List<Player> players = Arrays.asList(player1, player2, player3, player4);

        List<Player> citizens = players.stream()
                .filter(player -> player.getGameRole() == GameRole.CITIZEN)
                .collect(Collectors.toList());

        List<Player> liar = players.stream()
                .filter(player -> player.getGameRole() == GameRole.LIAR)
                .collect(Collectors.toList());


        //then
        assertThat(player1.getUserId()).isEqualTo("1");
        assertThat(citizens.size()).isEqualTo(3);
        assertThat(liar.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("gameId가 없으면 player의 role을 체크할 수 없고, NotFoundGameException을 발생시킨다.")
    public void checkPlayerRole_fail_notFoundGameId() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));

        //when
        gameService.save(setUpGameDto);

        //then
        Assertions.assertThatThrownBy(() -> {
            gameService.checkPlayerRole(new GameUserInfoDto("wrongId", "1"));
        })
                .isInstanceOf(NotFoundGameException.class);
    }

    @Test
    @DisplayName("userId가 없으면 player의 role을 체크할 수 없고, NotFoundUserException을 발생시킨다.")
    public void checkPlayerRole_fail_notFoundUserId() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));

        //when
        String gameId = gameService.save(setUpGameDto);

        //then
        Assertions.assertThatThrownBy(() -> {
                    gameService.checkPlayerRole(new GameUserInfoDto(gameId, "wrongUserId"));
                })
                .isInstanceOf(NotFoundUserException.class);
    }


    @Test
    @DisplayName("gameId와 userId가 올바르면, checkTopic 메소드를 실행해서, topic을 가져온다.")
    public void checkTopic_success() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));
        String gameId = gameService.save(setUpGameDto);

        //when
        Topic topic1 = gameService.checkTopic(new GameUserInfoDto(gameId, "1"));
        Topic topic2 = gameService.checkTopic(new GameUserInfoDto(gameId, "2"));
        Topic topic3 = gameService.checkTopic(new GameUserInfoDto(gameId, "3"));
        Topic topic4 = gameService.checkTopic(new GameUserInfoDto(gameId, "4"));

        List<Topic> topics = Arrays.asList(topic1, topic2, topic3, topic4);

        List<Topic> liarTopic = topics.stream()
                .filter(topic -> topic == null)
                .collect(Collectors.toList());

        List<Topic> citizensTopic = topics.stream()
                .filter(topic -> topic != null)
                .collect(Collectors.toList());

        //then
        assertThat(liarTopic.size()).isEqualTo(1);
        assertThat(citizensTopic.get(0).getTopicName()).isEqualTo(citizensTopic.get(1).getTopicName());
        assertThat(citizensTopic.get(1).getTopicName()).isEqualTo(citizensTopic.get(2).getTopicName());
    }


    @Test
    @DisplayName("gameId가 없다면, topic을 조회할 수 없고, NotFoundGameException이 발생한다.")
    public void checkTopic_fail_notFoundGameId() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));
        gameService.save(setUpGameDto);

        //when

        //then
        assertThatThrownBy(() -> {
            gameService.checkTopic(new GameUserInfoDto("wrongGameId", "1"));
        })
                .isInstanceOf(NotFoundGameException.class);
    }

    @Test
    @DisplayName("userId가 없다면, topic을 조회할 수 없고, NotFoundUserException이 발생한다.")
    public void checkTopic_fail_notFoundUserId() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));
        String gameId = gameService.save(setUpGameDto);

        //when

        //then
        assertThatThrownBy(() -> {
            gameService.checkTopic(new GameUserInfoDto(gameId, "wrongUserID"));
        })
                .isInstanceOf(NotFoundUserException.class);
    }
}