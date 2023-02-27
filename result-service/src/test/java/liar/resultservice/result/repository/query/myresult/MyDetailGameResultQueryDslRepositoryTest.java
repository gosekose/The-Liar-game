package liar.resultservice.result.repository.query.myresult;

import jakarta.transaction.Transactional;
import liar.resultservice.other.member.Member;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.MemberDummyInfo;
import liar.resultservice.result.domain.*;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MyDetailGameResultQueryDslRepositoryTest extends MemberDummyInfo {

    @Autowired
    MyDetailGameResultQueryDslRepository myDetailGameResultQueryDslRepository;
    @Autowired
    GameResultRepository gameResultRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PlayerResultRepository playerResultRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TopicRepository topicRepository;
    List<String> users = Arrays.asList(hostId, devUser1Id, devUser2Id, devUser3Id, devUser4Id, devUser5Id, devUser6Id);

    @BeforeEach
    public void init() {

        Topic topic = topicRepository.save(new Topic("coffee"));

        for (int i = 0; i < users.size(); i++) {
            Member member = memberRepository.findByUserId(users.get(i));
            playerRepository.save(Player.of(member));
        }

        for (int i = 0; i < 20; i++) {
            GameResult gameResult = gameResultRepository.save(GameResult
                    .builder().gameId(String.valueOf(i)).gameName("gameName: " + i)
                    .hostId(hostId).totalUsers(users.size()).roomId("roomId")
                    .winner(i % 2 == 0 ? GameRole.LIAR : GameRole.CITIZEN).topic(topic)
                    .build());
            playerResultRepository.save(new PlayerResult(gameResult, devUser1Id, GameRole.LIAR,
                    false, i % 2 == 0, 100L));
        }

    }

    @Test
    @DisplayName("fetchMyDetailGameResult의 onlyLastViews를 테스트 한다.")
    public void fetchMyDetailGameResult_onlyLastViews() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 10);

        //when
        MyDetailGameResultCond cond = new MyDetailGameResultCond(devUser1Id, true, null, null, null);
        Slice<MyDetailGameResultDto> myDetailGameResultDtos = myDetailGameResultQueryDslRepository.fetchMyDetailGameResult(cond, page);

        //then
        assertThat(myDetailGameResultDtos.getContent().get(0).getGameId()).isEqualTo("19");
        assertThat(myDetailGameResultDtos.getContent().get(0).getGameName()).isEqualTo("gameName: 19");
        assertThat(myDetailGameResultDtos.getContent().get(0).getAnswer()).isFalse();
        assertThat(myDetailGameResultDtos.getContent().get(0).getMyRole()).isEqualTo(GameRole.LIAR);
        assertThat(myDetailGameResultDtos.getContent().get(0).getWinner()).isEqualTo(GameRole.CITIZEN);
        assertThat(myDetailGameResultDtos.getContent().get(0).getTotalUsers()).isEqualTo(users.size());
        assertThat(myDetailGameResultDtos.getNumberOfElements()).isEqualTo(10);
        assertThat(myDetailGameResultDtos.getSize()).isEqualTo(10);
        assertThat(myDetailGameResultDtos.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("fetchMyDetailGameResult의 onlyWin을 테스트 한다.")
    public void fetchMyDetailGameResult_onlyWin() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 10);

        //when
        MyDetailGameResultCond cond = new MyDetailGameResultCond(devUser1Id, null, true, null, null);
        Slice<MyDetailGameResultDto> myDetailGameResultDtos = myDetailGameResultQueryDslRepository.fetchMyDetailGameResult(cond, page);

        //then
        assertThat(myDetailGameResultDtos.getContent().get(0).getAnswer()).isFalse();
        assertThat(myDetailGameResultDtos.getContent().get(0).getMyRole()).isEqualTo(GameRole.LIAR);
        assertThat(myDetailGameResultDtos.getContent().get(0).getWinner()).isEqualTo(GameRole.LIAR);
        assertThat(myDetailGameResultDtos.getContent().get(0).getTotalUsers()).isEqualTo(users.size());
        assertThat(myDetailGameResultDtos.getNumberOfElements()).isEqualTo(10);
        assertThat(myDetailGameResultDtos.getSize()).isEqualTo(10);
        assertThat(myDetailGameResultDtos.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("fetchMyDetailGameResult의 onlyLose을 테스트 한다.")
    public void fetchMyDetailGameResult_onlyLose() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 10);

        //when
        MyDetailGameResultCond cond = new MyDetailGameResultCond(devUser1Id, null, null, true, null);
        Slice<MyDetailGameResultDto> myDetailGameResultDtos = myDetailGameResultQueryDslRepository.fetchMyDetailGameResult(cond, page);

        //then
        assertThat(myDetailGameResultDtos.getContent().get(0).getAnswer()).isFalse();
        assertThat(myDetailGameResultDtos.getContent().get(0).getMyRole()).isEqualTo(GameRole.LIAR);
        assertThat(myDetailGameResultDtos.getContent().get(0).getWinner()).isEqualTo(GameRole.CITIZEN);
        assertThat(myDetailGameResultDtos.getContent().get(0).getTotalUsers()).isEqualTo(users.size());
        assertThat(myDetailGameResultDtos.getNumberOfElements()).isEqualTo(10);
        assertThat(myDetailGameResultDtos.getSize()).isEqualTo(10);
        assertThat(myDetailGameResultDtos.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("fetchMyDetailGameResult의 searchGameName을 테스트 한다.")
    public void fetchMyDetailGameResult_searchGameName() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 10);

        //when
        MyDetailGameResultCond cond = new MyDetailGameResultCond(devUser1Id, null, null, null, "gameName");
        Slice<MyDetailGameResultDto> myDetailGameResultDtos = myDetailGameResultQueryDslRepository.fetchMyDetailGameResult(cond, page);

        //then
        assertThat(myDetailGameResultDtos.getContent().get(0).getAnswer()).isFalse();
        assertThat(myDetailGameResultDtos.getContent().get(0).getMyRole()).isEqualTo(GameRole.LIAR);
        assertThat(myDetailGameResultDtos.getContent().get(0).getTotalUsers()).isEqualTo(users.size());
        assertThat(myDetailGameResultDtos.getNumberOfElements()).isEqualTo(10);
        assertThat(myDetailGameResultDtos.getSize()).isEqualTo(10);
        assertThat(myDetailGameResultDtos.getNumber()).isEqualTo(0);
    }

}