package liar.gamemvcservice.game.service;

import liar.gamemvcservice.exception.exception.NotFoundGameException;
import liar.gamemvcservice.game.controller.dto.GameUserInfoDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameTurnDto;
import liar.gamemvcservice.game.domain.*;
import liar.gamemvcservice.game.repository.GameRepository;
import liar.gamemvcservice.game.repository.GameTurnRepository;
import liar.gamemvcservice.game.service.dto.GameTurnResponse;
import liar.gamemvcservice.game.service.player.PlayerPolicy;
import liar.gamemvcservice.game.service.topic.TopicPolicy;
import liar.gamemvcservice.game.service.turn.PlayerTurnPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final TopicPolicy topicPolicy;
    private final PlayerPolicy playerPolicy;
    private final PlayerTurnPolicy playerTurnPolicy;
    private final GameTurnRepository gameTurnRepository;

    public String save(SetUpGameDto dto) {
        Game notSetUpTopicGame = Game.of(dto);
        Topic topic = topicPolicy.setUp();
        Game setUpTopicGame = notSetUpTopicGame.updateTopicOfGame(topic);
        playerPolicy.setUpPlayerRole(setUpTopicGame);
        return gameRepository.save(setUpTopicGame).getId();
    }

    public Player checkPlayerRole(GameUserInfoDto dto) {
        return playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());
    }

    public Topic checkTopic(GameUserInfoDto dto) {
        Player player = playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());

        if (player.getGameRole() == GameRole.CITIZEN) {
            Game game = gameRepository.findById(dto.getGameId()).orElseThrow(NotFoundGameException::new);
            return game.getTopic();
        }

        return null;
    }

    public GameTurnResponse setUpTurn(SetUpGameTurnDto dto) {
        Game game = gameRepository.findById(dto.getGameId()).orElseThrow(NotFoundGameException::new);
        return GameTurnResponse.of(playerTurnPolicy.setUpTurn(game));
    }

}
