package liar.waitservice.wait.controller;

import com.google.gson.Gson;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.PostProcessEndGameDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteJoinMemberRepository;
import liar.waitservice.wait.repository.rdbms.WaitRoomCompleteRepository;
import liar.waitservice.wait.repository.redis.JoinMemberRedisRepository;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import liar.waitservice.wait.service.start.DoProcessStartAndEndGameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DoProcessStartAndEndGameControllerDocsTest extends CommonController {

    @Autowired
    DoProcessStartAndEndGameService doProcessStartAndEndGameService;
    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;
    @Autowired
    JoinMemberRedisRepository joinMemberRedisRepository;
    @Autowired
    WaitRoomCompleteRepository waitRoomCompleteRepository;
    @Autowired
    WaitRoomCompleteJoinMemberRepository waitRoomCompleteJoinMemberRepository;
    private WaitRoom waitRoom;

    @Override
    public void tearDown() {
        super.tearDown();
        joinMemberRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("doPreProcessBeforeGameStart 컨트롤러 요청이 오면, 게임 시작 전 preProcess를 진행하고 메세지를 리턴한다.")
    public void doPreProcessBeforeGameStartDocs() throws Exception {
        //given
        pushData();
        RequestWaitRoomDto request = new RequestWaitRoomDto(hostId, waitRoom.getId());

        //when
        ResultActions perform = mockMvc.perform(
                post("/wait-service/game/start")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(new Gson().toJson(request))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", "userId")
        );

        //then
        perform
                .andExpect(status().isOk())
                .andDo(document("waitroom-start",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("userId").type(STRING).description("호스트 아이디"),
                            fieldWithPath("roomId").type(STRING).description("대기실 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("status").type(BOOLEAN).description("상태 결과")
                        )));
    }

    @Test
    @DisplayName("doPostProcessAfterGameEnd 컨트롤러 요청이 오면, 게임 시작 후 postProcess를 진행하고 메세지를 리턴한다.")
    public void doPostProcessAfterGameEndDocs() throws Exception {
//given
        pushData();
        RequestWaitRoomDto request = new RequestWaitRoomDto(hostId, waitRoom.getId());
        doProcessStartAndEndGameService.doPreProcessBeforeGameStart(request);

        PostProcessEndGameDto endGameDto = new PostProcessEndGameDto(waitRoom.getId());

        //when
        ResultActions perform = mockMvc.perform(
                post("/wait-service/game/end")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(new Gson().toJson(endGameDto))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", "userId")
        );

        //then
        perform
                .andExpect(status().isOk())
                .andDo(document("waitroom-end",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("roomId").type(STRING).description("대기실 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("status").type(BOOLEAN).description("상태 결과")
                        )));

    }


    private void pushData() {
        waitRoom = WaitRoom.of(new CreateWaitRoomDto(hostId, "koseGame", 7), "gosekose");
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom));

        waitRoom.joinMembers(devUser1Id);
        waitRoom.joinMembers(devUser2Id);
        waitRoom.joinMembers(devUser3Id);
        waitRoom.joinMembers(devUser4Id);
        waitRoom.joinMembers(devUser5Id);
        waitRoom.joinMembers(devUser6Id);
        waitRoomRedisRepository.save(waitRoom);

        joinMemberRedisRepository.save(JoinMember.of(devUser1Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser2Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser3Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser4Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser5Id, waitRoom.getId()));
        joinMemberRedisRepository.save(JoinMember.of(devUser6Id, waitRoom.getId()));
    }

}