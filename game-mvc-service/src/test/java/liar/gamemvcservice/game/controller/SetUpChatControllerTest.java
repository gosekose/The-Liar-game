package liar.gamemvcservice.game.controller;

import com.google.gson.Gson;
import liar.gamemvcservice.game.service.dto.CommonDto;
import liar.gamemvcservice.game.service.dto.SetUpGameDto;
import liar.gamemvcservice.game.service.GameFacadeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static javax.management.openmbean.SimpleType.STRING;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class SetUpChatControllerTest extends CommonController {

    @Autowired
    GameFacadeServiceImpl gameFacadeServiceImpl;

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
        joinPlayerRepository.deleteAll();
    }

    @Test
    @DisplayName("post: setUpGame")
    public void setUpGame() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        String roomName = "kose-game";
        List<String> userIds = Arrays.asList(hostId, devUser1Id, devUser2Id, devUser3Id, devUser4Id);

        SetUpGameDto setUpGameDto = new SetUpGameDto(roomId, hostId, roomName, userIds);

        //when
        ResultActions result = mockMvc.perform(
                post("/game-service/game/setup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(setUpGameDto))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", "user-id"));

        //then
        result.andExpect(status().isOk())
                .andDo(document("game-setup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("roomId").type(STRING).description("대기실 Id"),
                                fieldWithPath("hostId").type(STRING).description("대기실 호스트 Id"),
                                fieldWithPath("roomName").type(STRING).description("대기실 이름"),
                                fieldWithPath("userIds").type(List.class).description("게임 참여 인원")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                                fieldWithPath("body").type(JsonFieldType.STRING).description("게임 Id")
                        )));
    }
    
    @Test
    @DisplayName("get: checkUserRole")
    public void checkUserRole() throws Exception {
        //given
        String gameId = saveGameAndGetGameId();
        CommonDto commonDto = new CommonDto(gameId, devUser1Id);

        //when
        ResultActions result = mockMvc.perform(
                get("/game-service/game/{userId}/role", commonDto.getUserId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(commonDto))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", devUser1Id));

        //then
        result.andExpect(status().isOk())
                .andDo(document("game-check-userRole",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("header의 userId와 동일한 role 정보를 요청하는 userId")
                        ),
                        requestFields(
                                fieldWithPath("gameId").type(STRING).description("게임 Id"),
                                fieldWithPath("userId").type(STRING).description("유저 Id")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                                fieldWithPath("body.userId").type(JsonFieldType.STRING).description("요청 유저 Id"),
                                fieldWithPath("body.gameRole").type(JsonFieldType.STRING).description("유저 Id 게임 역할")
                        )));
        
    }

    @Test
    @DisplayName("get: checkTopic")
    public void checkTopic() throws Exception {
        //given
        String gameId = saveGameAndGetGameId();
        CommonDto commonDto = new CommonDto(gameId, devUser1Id);

        //when
        ResultActions result = mockMvc.perform(
                get("/game-service/game/{userId}/topic", commonDto.getUserId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(commonDto))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", devUser1Id));

        //then
        result.andExpect(status().isOk())
                .andDo(document("game-check-topic",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("header의 userId와 동일한 role 정보를 요청하는 userId")
                        ),
                        requestFields(
                                fieldWithPath("gameId").type(STRING).description("게임 Id"),
                                fieldWithPath("userId").type(STRING).description("유저 Id")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                                fieldWithPath("body").type(JsonFieldType.STRING).description("게임 주제")
                        )));
    }

    private String saveGameAndGetGameId() {
        String roomId = UUID.randomUUID().toString();
        String roomName = "kose-game";
        List<String> userIds = Arrays.asList(hostId, devUser1Id, devUser2Id, devUser3Id, devUser4Id);

        SetUpGameDto setUpGameDto = new SetUpGameDto(roomId, hostId, roomName, userIds);
        return gameFacadeServiceImpl.save(setUpGameDto);
    }

}