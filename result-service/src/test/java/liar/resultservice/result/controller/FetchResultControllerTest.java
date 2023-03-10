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
                                fieldWithPath("code").type(STRING).description("?????? ??????"),
                                fieldWithPath("message").type(STRING).description("?????? ?????????"),
                                fieldWithPath("body.content[].userId").type(STRING).description("?????? ?????????"),
                                fieldWithPath("body.content[].userName").type(STRING).description("?????? ??????"),
                                fieldWithPath("body.content[].level").type(STRING).description("?????? ??????").attributes(
                                        key("constraints").value(Arrays.asList("BRONZE1", "BRONZE2", "SILVER1", "SILVER2", "GOLD1", "GOLD2", "PLATINUM", "MASTER", "LIAR"))),
                                fieldWithPath("body.content[].exp").type(LONG).description("?????? ?????????"),
                                fieldWithPath("body.content[].wins").type(LONG).description("?????? ??????"),
                                fieldWithPath("body.content[].loses").type(LONG).description("?????? ??????"),
                                fieldWithPath("body.content[].totalGames").type(LONG).description("??? ?????? ??????")
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
                                fieldWithPath("userId").type(STRING).description("?????? ?????????"),
                                fieldWithPath("viewLatest").type(BOOLEAN).description("?????? ??? ??????"),
                                fieldWithPath("viewOnlyWin").type(BOOLEAN).description("????????? ????????? ??????"),
                                fieldWithPath("viewOnlyLose").type(BOOLEAN).description("????????? ????????? ??????"),
                                fieldWithPath("searchGameName").type(STRING).description("?????? ????????? ????????? ????????? ??????")
                        ),
                        responseFieldsSnippetPageable(
                                fieldWithPath("code").type(STRING).description("?????? ??????"),
                                fieldWithPath("message").type(STRING).description("?????? ?????????"),
                                fieldWithPath("body.content[].gameId").type(STRING).description("?????? ?????????"),
                                fieldWithPath("body.content[].gameName").type(STRING).description("?????? ??????"),
                                fieldWithPath("body.content[].topicName").type(STRING).description("??????"),
                                fieldWithPath("body.content[].winner").type(STRING).description("????????? ??????"),
                                fieldWithPath("body.content[].totalUsers").type(LONG).description("??? ?????? ???"),
                                fieldWithPath("body.content[].myRole").type(STRING).description("??? ??????"),
                                fieldWithPath("body.content[].answer").type(BOOLEAN).description("??? ?????? ??????")
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