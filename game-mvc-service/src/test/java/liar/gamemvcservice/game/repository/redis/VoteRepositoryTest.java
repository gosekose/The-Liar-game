package liar.gamemvcservice.game.repository.redis;

import jakarta.ws.rs.NotFoundException;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.Vote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VoteRepositoryTest {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    GameRepository gameRepository;

    private Game game;

    @BeforeEach
    public void init() {
        SetUpGameDto setUpGameDto = new SetUpGameDto("1", "1", "1", Arrays.asList("1", "2", "3", "4", "5"));
        game = Game.of(setUpGameDto);
        Game save = gameRepository.save(game);
    }

//    @AfterEach
//    public void tearDown() {
//        gameRepository.deleteAll();
//        voteRepository.deleteAll();
//    }

    @Test
    @DisplayName("save")
    public void save() throws Exception {
        //given
        Vote vote = new Vote(game.getId(), game.getPlayerIds());
        ;
        //when
        Vote savedVote = voteRepository.save(vote);
        Vote findVote = voteRepository.findVoteByGameId(game.getId());

        //then
        assertThat(savedVote.getGameId()).isEqualTo(game.getId());
    }

}