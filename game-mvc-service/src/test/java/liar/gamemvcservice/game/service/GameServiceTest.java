package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.controller.dto.RequestCommonDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        String gameId = gameService.save(setUpGameDto);
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
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));
        String gameId = gameService.save(setUpGameDto);

        //when
        List<Player> players = getPlayers(gameId);
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
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));

        //when
        gameService.save(setUpGameDto);

        //then
        Assertions.assertThatThrownBy(() -> {
            gameService.checkPlayerRole(new RequestCommonDto("wrongId", "1"));
        }).isInstanceOf(NotFoundGameException.class);
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
                    gameService.checkPlayerRole(new RequestCommonDto(gameId, "wrongUserId"));
                }).isInstanceOf(NotFoundUserException.class);
    }


    @Test
    @DisplayName("gameId와 userId가 올바르면, checkTopic 메소드를 실행해서, topic을 가져온다.")
    public void checkTopic_success() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));
        String gameId = gameService.save(setUpGameDto);

        //when
        List<Topic> topics = getTopics(gameId);
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
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));

        //when
        gameService.save(setUpGameDto);

        //then
        assertThatThrownBy(() -> {
            gameService.checkTopic(new RequestCommonDto("wrongGameId", "1"));
        }).isInstanceOf(NotFoundGameException.class);
    }

    @Test
    @DisplayName("userId가 없다면, topic을 조회할 수 없고, NotFoundUserException이 발생한다.")
    public void checkTopic_fail_notFoundUserId() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("roomId", "1", "gameName", Arrays.asList("1", "2", "3", "4"));

        //when
        String gameId = gameService.save(setUpGameDto);

        //then
        assertThatThrownBy(() -> {
            gameService.checkTopic(new RequestCommonDto(gameId, "wrongUserID"));
        }).isInstanceOf(NotFoundUserException.class);
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
        for (int i = 0; i < 4; i++) topics.add(gameService.checkTopic(new RequestCommonDto(gameId, String.valueOf(i))));
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
        for (int i = 0; i < 4; i++) players.add(gameService.checkPlayerRole(new RequestCommonDto(gameId, "1")));
        return players;
    }
}