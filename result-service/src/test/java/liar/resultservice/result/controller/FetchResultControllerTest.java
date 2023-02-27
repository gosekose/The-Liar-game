package liar.resultservice.result.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import liar.resultservice.other.member.Member;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.result.controller.dto.request.MyDetailGameResultRequest;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.domain.PlayerResult;
import liar.resultservice.result.service.ResultFacadeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static javax.management.openmbean.SimpleType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FetchResultControllerTest extends CommonController {

    @Autowired
    ResultFacadeService resultFacadeService;
    @Autowired
    ObjectMapper objectMapper;

    List<String> users = Arrays.asList(hostId, devUser1Id, devUser2Id, devUser3Id, devUser4Id, devUser5Id, devUser6Id);


    @Test
    @DisplayName("RestDocs: fetchPlayerRank / Get")
    public void fetchPlayerRank() throws Exception {
        //given
        setUpForRanking();
        Pageable page = PageRequest.of(0, 10);

        //when
        ResultActions result = mockMvc.perform(
                get("/result-service/result/rank")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(page))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", "user-id"));

        //then
        result.andExpect(status().isOk())
                .andDo(document("result-rank",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFieldsSnippetPageable(
                                fieldWithPath("code").type(STRING).description("상태 코드"),
                                fieldWithPath("message").type(STRING).description("상태 메세지"),
                                fieldWithPath("body.content[].userId").type(STRING).description("유저 아이디"),
                                fieldWithPath("body.content[].userName").type(STRING).description("유저 이름"),
                                fieldWithPath("body.content[].level").type(STRING).description("유저 레벨").attributes(
                                        key("constraints").value(Arrays.asList("BRONZE1", "BRONZE2", "SILVER1", "SILVER2", "GOLD1", "GOLD2", "PLATINUM", "MASTER", "LIAR"))),
                                fieldWithPath("body.content[].exp").type(LONG).description("유저 경험차"),
                                fieldWithPath("body.content[].wins").type(LONG).description("승리 횟수"),
                                fieldWithPath("body.content[].loses").type(LONG).description("패배 횟수"),
                                fieldWithPath("body.content[].totalGames").type(LONG).description("총 게임 횟수")
                        )
                ));


    }

    @Test
    @DisplayName("RestDocs: fetchMyDetailGameResult / Get")
    public void fetchMyDetailGameResult() throws Exception {
        //given
        setUpForMyResult();
        MyDetailGameResultRequest request = new MyDetailGameResultRequest(devUser1Id, null, true, null, null);

        //when
        ResultActions result = mockMvc.perform(
                get("/result-service/result/{userId}", devUser1Id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", devUser1Id));

        //then
        result.andExpect(status().isOk())
                .andDo(document("result-myResult",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(STRING).description("유저 아이디"),
                                fieldWithPath("viewLatest").type(BOOLEAN).description("최신 순 조회"),
                                fieldWithPath("viewOnlyWin").type(BOOLEAN).description("승리한 경기만 조회"),
                                fieldWithPath("viewOnlyLose").type(BOOLEAN).description("패배한 경기만 조회"),
                                fieldWithPath("searchGameName").type(STRING).description("게임 이름을 포함한 경기만 조회")
                        ),
                        responseFieldsSnippetPageable(
                                fieldWithPath("code").type(STRING).description("상태 코드"),
                                fieldWithPath("message").type(STRING).description("상태 메세지"),
                                fieldWithPath("body.content[].gameId").type(STRING).description("게임 아이디"),
                                fieldWithPath("body.content[].gameName").type(STRING).description("게임 이름"),
                                fieldWithPath("body.content[].topicName").type(STRING).description("주제"),
                                fieldWithPath("body.content[].winner").type(STRING).description("승리한 역할"),
                                fieldWithPath("body.content[].totalUsers").type(LONG).description("총 유저 수"),
                                fieldWithPath("body.content[].myRole").type(STRING).description("내 역할"),
                                fieldWithPath("body.content[].answer").type(BOOLEAN).description("내 투표 정답")
                        )));

    }

    private void setUpForRanking() {
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

    public void setUpForMyResult() {

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
}