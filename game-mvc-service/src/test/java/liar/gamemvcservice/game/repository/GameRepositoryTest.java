package liar.gamemvcservice.game.repository;

import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
    }

    @Test
    @DisplayName("Game을 저장하면, redis에 저장되어야 한다.")
    public void save_game_success() throws Exception {
        //given
        SetUpGameDto setUpGameDto = new SetUpGameDto("1", "1", "1", Arrays.asList("2", "3", "4"));
        Game game = Game.of(setUpGameDto);

        //when
        Game savedGame = gameRepository.save(game);

        //then
        assertThat(savedGame.getId()).isEqualTo(game.getId());
        assertThat(savedGame.getGameName()).isEqualTo(game.getGameName());
        assertThat(savedGame.getHostId()).isEqualTo(game.getHostId());
        assertThat(savedGame.getRoomId()).isEqualTo(game.getRoomId());
        assertThat(savedGame.getPlayerIds()).isEqualTo(game.getPlayerIds());
    }

}