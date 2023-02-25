package liar.resultservice.result.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import liar.resultservice.exception.exception.NotFoundGameException;
import liar.resultservice.exception.exception.NotFoundUserException;
import liar.resultservice.other.member.Member;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.MemberDummyInfo;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.controller.dto.request.VotedResultDto;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.domain.PlayerResult;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static liar.resultservice.result.domain.Level.GOLD2;
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
    SaveResultRequest request;
    Topic topic;
    @Autowired
    private PlayerResultRepository playerResultRepository;

    @BeforeEach
    public void init() {
        topic = topicRepository.save(new Topic("game"));
        topicRepository.flush();

        createVotedResultDtos();
        createPlayerResultInfoDtos();
        request = new SaveResultRequest("gameId", GameRole.LIAR, playerResultInfoDtos, "roomId", "gameName",
                hostId, topic.getId(), playerResultInfoDtos.size(), votedResultDtos);
    }


    @Test
    @DisplayName("단일 스레드에서 saveGameResult")
    public void saveGameResult_single() throws Exception {
        //given
        GameResult gameResult = resultFacadeService.saveGameResult(request);

        //when
        GameResult findGameResult = gameResultRepository.findById(gameResult.getId()).orElseThrow(NotFoundGameException::new);

        //then
        assertThat(gameResult.getId()).isEqualTo(findGameResult.getId());
        assertThat(gameResult.getGameId()).isEqualTo(findGameResult.getGameId());
    }

    @Test
    @DisplayName("멀티 스레드에서 saveGameResult")
    public void saveGameResult_multiThread() throws Exception {
        //given
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        GameResult[] gameResults = new GameResult[threadCount];

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {
                try {
                    gameResults[finalIdx] = resultFacadeService
                            .saveGameResult(makeSaveResultRequest(UUID.randomUUID().toString()));
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        Set<String> idSet = new HashSet<>();
        for (int i = 0; i < threadCount; i++) {
            String id = gameResults[i].getId();
            System.out.println("id = " + id);
            idSet.add(id);
        }

        //then
        assertThat(idSet.size()).isEqualTo(threadCount);
    }

    @Test
    @DisplayName("단일 스레드에서 savePlayer")
    public void savePlayer_single() throws Exception {
        //given
        GameResult gameResult = resultFacadeService.saveGameResult(request);
        Player player = getPlayer(playerResultInfoDtos.get(0));

        //when
        resultFacadeService.savePlayer(gameResult, player, GameRole.LIAR, 300L);
        Player findPlayer = playerRepository.findById(player.getId()).orElseThrow(NotFoundUserException::new);

        //then
        assertThat(player.getId()).isEqualTo(findPlayer.getId());
        assertThat(player.getExp()).isEqualTo(findPlayer.getExp());
    }

    @Test
    @DisplayName("멀티 스레드에서 savePlayer")
    public void savePlayer_multiThread() throws Exception {
        //given
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        GameResult gameResult = resultFacadeService.saveGameResult(request);
        Player player = getPlayer(playerResultInfoDtos.get(0));

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {
                try{
                    resultFacadeService.savePlayer(gameResult, player, GameRole.LIAR, 300L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        Player findPlayer = playerRepository.findById(player.getId()).orElseThrow();

        //then
        assertThat(findPlayer.getExp()).isEqualTo(1500L);
        assertThat(findPlayer.getLevel()).isEqualTo(GOLD2);
    }

    @Test
    @DisplayName("단일 스레드에서 savePlayerResult")
    public void savePlayerResult_single() throws Exception {
        //given
        int count = 4;
        GameResult gameResult = resultFacadeService.saveGameResult(request);

        //when
        for (int i = 0; i < count; i++) {
            getPlayer(playerResultInfoDtos.get(i));
        }

        for (int i = 0; i < count; i++) {
            resultFacadeService.
                    savePlayerResult(request, gameResult.getId(), playerResultInfoDtos.get(i), 100L);
        }


        List<PlayerResult> playerResults = playerResultRepository.findPlayerResultsByGameResult(gameResult);

        //then
        assertThat(playerResults.size()).isEqualTo(count);
        assertThat(playerResults.get(0).getGameResult().getId()).isEqualTo(gameResult.getId());
        assertThat(playerResults.get(0).getExp()).isEqualTo(100L);
    }

    @Test
    @DisplayName("멀티 스레드에서 savePlayerResult")
    @Transactional
    public void savePlayerResult_multiThread() throws Exception {
        //given
        int threadCount = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        GameResult gameResult = resultFacadeService.saveGameResult(request);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        System.out.println("gameResult = " + gameResult.getId());

        for (int i = 0; i < threadCount; i++) {
            getPlayer(playerResultInfoDtos.get(i));
        }

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {
               try{
                   resultFacadeService.
                           savePlayerResult(request, gameResult.getId(), playerResultInfoDtos.get(finalIdx), 100L);
               } catch (Exception e) {
                   e.printStackTrace();
                   e.getCause();
               }
               finally {
                   countDownLatch.countDown();
               }
            });
        }

        countDownLatch.await();

        //then
        GameResult findGameResult = gameResultRepository.findById(gameResult.getId()).orElseThrow();
        System.out.println("findGameResult.getId() = " + findGameResult.getId());
        List<PlayerResult> playerResultsByGameResult = playerResultRepository.findPlayerResultsByGameResult(findGameResult);
        assertThat(playerResultsByGameResult.size()).isEqualTo(4);

    }

    @Test
    @DisplayName("saveAllResultOfGame")
    public void saveAllResultOfGame_single() throws Exception {
        //given
        int multiRequest = 10;
        GameResult[] gameResults = new GameResult[multiRequest];

        //when
        for (int i = 0; i < multiRequest; i++) {
            request = new SaveResultRequest(String.valueOf(i), GameRole.LIAR, playerResultInfoDtos, "roomId", "gameName",
                    hostId, topic.getId(), playerResultInfoDtos.size(), votedResultDtos);
            resultFacadeService.saveAllResultOfGame(request);
        }

        GameResult gameResult = gameResultRepository.findGameResultByGameId(request.getGameId());
        List<PlayerResult> playerResults = playerResultRepository.findPlayerResultsByGameResult(gameResult);
        GameResult findGameResult = gameResultRepository.findGameResultByGameId("gameId");
        memberRepository.findByUserId(devUser1Id);

        //then
        assertThat(findGameResult).isNotNull();
        assertThat(findGameResult.getGameName()).isNotNull();
        assertThat(gameResult.getHostId()).isEqualTo(hostId);
        assertThat(playerResults.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("saveAllResultOfGame_multiThread")
    public void saveAllResultOfGame_multiThread() throws Exception {
        //given
        int threadCount = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {
                try{
                    Topic savedTopic = topicRepository.save(new Topic("test"));
                    SaveResultRequest resultTest = new SaveResultRequest(String.valueOf(finalIdx), GameRole.LIAR, playerResultInfoDtos,
                            "roomId", "gameName",
                            hostId, savedTopic.getId(), playerResultInfoDtos.size(), votedResultDtos);
                    resultFacadeService.saveAllResultOfGame(resultTest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        GameResult gameResult1 = gameResultRepository.findGameResultByGameId("0");
        GameResult gameResult2 = gameResultRepository.findGameResultByGameId("1");
        GameResult gameResult3 = gameResultRepository.findGameResultByGameId("2");
        GameResult gameResult4 = gameResultRepository.findGameResultByGameId("3");
        System.out.println("gameResult4.getId() = " + gameResult1.getId());
        System.out.println("gameResult4.getId() = " + gameResult2.getId());
        System.out.println("gameResult4.getId() = " + gameResult3.getId());
        System.out.println("gameResult4.getId() = " + gameResult4.getId());
        List<PlayerResult> playerResults1 = playerResultRepository.findPlayerResultsByGameResult(gameResult1);
        List<PlayerResult> playerResults2 = playerResultRepository.findPlayerResultsByGameResult(gameResult2);
        List<PlayerResult> playerResults3 = playerResultRepository.findPlayerResultsByGameResult(gameResult3);
        List<PlayerResult> playerResults4 = playerResultRepository.findPlayerResultsByGameResult(gameResult4);
        Player playerByMember = playerRepository.findPlayerByMember(memberRepository.findByUserId(devUser1Id));

        //then
        assertThat(gameResult1.getHostId()).isEqualTo(hostId);
        assertThat(playerResults1.size()).isEqualTo(4);
        assertThat(gameResult2.getHostId()).isEqualTo(hostId);
        assertThat(playerResults2.size()).isEqualTo(4);
        assertThat(gameResult3.getHostId()).isEqualTo(hostId);
        assertThat(playerResults3.size()).isEqualTo(4);
        assertThat(gameResult4.getHostId()).isEqualTo(hostId);
        assertThat(playerResults4.size()).isEqualTo(4);
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
        topic = topicRepository.save(new Topic("game"));
        return new SaveResultRequest(gameId, GameRole.LIAR, playerResultInfoDtos, "roomId", "gameName",
                hostId, topic.getId(), playerResultInfoDtos.size(), votedResultDtos);
    }


    private Player getPlayer(PlayerResultInfoDto dto) {
        Member member = getMember(dto);
        Player player = playerRepository.findPlayerByMember(member);
        if (player == null) {
            return playerRepository.save(Player.of(member));
        }
        return player;
    }

    private Member getMember(PlayerResultInfoDto dto) {
        Member member = memberRepository.findByUserId(dto.getUserId());
        if (member == null) {
            throw new NotFoundUserException();
        }
        return member;
    }
}