package liar.gamemvcservice.game.service.result;

import liar.gamemvcservice.exception.exception.NotFoundUserException;
import liar.gamemvcservice.game.service.dto.GameResultToServerDto;
import liar.gamemvcservice.game.service.dto.*;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.repository.redis.GameRepository;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import liar.gamemvcservice.game.repository.redis.VoteRepository;
import liar.gamemvcservice.game.service.ThreadServiceOnlyTest;
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
}