package liar.gamemvcservice.game.service;

import liar.gamemvcservice.game.controller.dto.CheckUserRoleDto;
import liar.gamemvcservice.game.controller.dto.SetUpGameDto;
import liar.gamemvcservice.game.domain.Game;
import liar.gamemvcservice.game.domain.Player;
import liar.gamemvcservice.game.domain.Topic;
import liar.gamemvcservice.game.repository.GameRepository;
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

    public String save(SetUpGameDto dto) {
        Game notSetUpTopicGame = Game.of(dto);
        Topic topic = topicPolicy.setUp();
        Game setUpTopicGame = notSetUpTopicGame.updateTopicOfGame(topic);
        playerPolicy.setUpPlayerRole(setUpTopicGame);
        return gameRepository.save(setUpTopicGame).getId();
    }

    public Player checkPlayerRole(CheckUserRoleDto dto) {
        return playerPolicy.checkPlayerInfo(dto.getGameId(), dto.getUserId());
    }

}
