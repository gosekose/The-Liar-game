package liar.resultservice.result.service;

import liar.resultservice.exception.exception.NotFoundGameException;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.MemberDummyInfo;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.controller.dto.request.VotedResultDto;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
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

@SpringBootTest
@Transactional
class ResultFacadeServiceImplTest extends MemberDummyInfo {

    @Autowired
    ResultFacadeService resultFacadeService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    GameResultRepository gameResultRepository;

    @Autowired
    PlayerRepository playerRepository;

    List<PlayerResultInfoDto> playerResultInfoDtos = new ArrayList<>();
    List<VotedResultDto>votedResultDtos = new ArrayList<>();
    SaveResultRequest saveResultRequest;

    @BeforeEach
    public void init() {
        Topic topic = topicRepository.save(new Topic("game"));
        createVotedResultDtos();
        createPlayerResultInfoDtos();
        saveResultRequest = new SaveResultRequest("gameId", GameRole.LIAR, playerResultInfoDtos, "roomId", "gameName",
                hostId, topic.getId(), playerResultInfoDtos.size(), votedResultDtos);
    }
    
    @Test
    @DisplayName("단일 스레드에서 saveGameResult")
    public void saveGameResult() throws Exception {
        //given
        GameResult gameResult = resultFacadeService.saveGameResult(saveResultRequest);

        //when
        GameResult findGameResult = gameResultRepository.findById(gameResult.getId()).orElseThrow(NotFoundGameException::new);

        //then
        assertThat(gameResult.getId()).isEqualTo(findGameResult.getId());
        assertThat(gameResult.getGameId()).isEqualTo(findGameResult.getGameId());
    }


    private void createPlayerResultInfoDtos() {
        playerResultInfoDtos.add(new PlayerResultInfoDto(hostId, GameRole.LIAR, false));
        playerResultInfoDtos.add(new PlayerResultInfoDto(devUser1Id, GameRole.CITIZEN, false));
        playerResultInfoDtos.add(new PlayerResultInfoDto(devUser2Id, GameRole.CITIZEN, false));
        playerResultInfoDtos.add(new PlayerResultInfoDto(devUser3Id, GameRole.CITIZEN, false));
    }

    private void createVotedResultDtos() {
        votedResultDtos.add(new VotedResultDto(hostId, Arrays.asList(devUser1Id), 1));
        votedResultDtos.add(new VotedResultDto(devUser1Id, Arrays.asList(devUser2Id, devUser3Id), 2));
        votedResultDtos.add(new VotedResultDto(devUser2Id, Arrays.asList(hostId), 1));
        votedResultDtos.add(new VotedResultDto(devUser3Id, Arrays.asList(), 0));
    }

    private SaveResultRequest makeSaveResultRequest(String gameId) {
        Topic topic = topicRepository.save(new Topic("game"));
        return new SaveResultRequest("gameId", GameRole.LIAR, playerResultInfoDtos, "roomId", "gameName",
                hostId, topic.getId(), playerResultInfoDtos.size(), votedResultDtos);
    }
}