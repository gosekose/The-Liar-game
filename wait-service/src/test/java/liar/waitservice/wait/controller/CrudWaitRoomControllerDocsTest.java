package liar.waitservice.wait.controller;

import com.google.gson.Gson;
import liar.waitservice.exception.exception.NotFoundWaitRoomException;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.redis.JoinMemberRedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CrudWaitRoomControllerDocsTest extends CommonController {

    @Autowired
    JoinMemberRedisRepository joinMemberRedisRepository;

    @Test
    @DisplayName("WaitRoom save 요청이 오면, waitRoom을 redis에 저장한 후, waitRoomId를 리턴한다.")
    public void saveWaitRoomByHost() throws Exception {
        //given
        CreateWaitRoomDto CreateRequest = new CreateWaitRoomDto(hostId, "game", 7);

        //when
        ResultActions result = mockMvc.perform(
                post("/wait-service/waitroom/create")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(new Gson().toJson(CreateRequest))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", "user-id"));

        //then
        result.andExpect(status().isOk())
                .andDo(document("waitroom-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(STRING).description("유저 아이디"),
                                fieldWithPath("roomName").type(STRING).description("방 이름"),
                                fieldWithPath("limitMembers").type(NUMBER).description("인원수 제한")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("waitRoomId").type(STRING).description("대기방 Id")
                        )));
        
    }

    @Test
    @DisplayName("호스트가 방 제거 요청을 하면, 방을 제거하고 성공하면 deleteStatus true를 반환한다.")
    public void deleteWaitRoomByHost() throws Exception {
        //given
        WaitRoom waitRoom = WaitRoom.of(new CreateWaitRoomDto(hostId, "game", 7), "roomName");
        waitRoomRedisRepository.save(waitRoom);

        RequestWaitRoomDto DeleteRequest = new RequestWaitRoomDto(hostId, waitRoom.getId());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/wait-service/waitroom/delete")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(new Gson().toJson(DeleteRequest))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "RefreshToken")
                        .header("userId", "userId")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("waitroom-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(STRING).description("유저 아이디"),
                                fieldWithPath("roomId").type(STRING).description("방 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("deleteStatus").type(BOOLEAN).description("삭제 상태")
                        )));

    }
    
    @Test
    @DisplayName("방 가입 요청을 하면, 방 가입 요청을 처리하여 boolean 결과를 전달한다.")
    public void joinMembers() throws Exception {
        //given
        WaitRoom waitRoom = WaitRoom.of(new CreateWaitRoomDto(hostId, "game", 7), "roomName");
        waitRoomRedisRepository.save(waitRoom);

        RequestWaitRoomDto joinRequest = new RequestWaitRoomDto(devUser1Id, waitRoom.getId());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/wait-service/waitroom/join")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(new Gson().toJson(joinRequest))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "RefreshToken")
                        .header("userId", "userId")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("waitroom-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(STRING).description("유저 아이디"),
                                fieldWithPath("roomId").type(STRING).description("방 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("joinStatus").type(BOOLEAN).description("조인 상태")
                        )));
    }


    @Test
    @DisplayName("방 퇴장 요청을 하면, 방 퇴장 요청을 처리하여 boolean 결과를 전달한다.")
    public void leaveMember() throws Exception {
        //given
        WaitRoom waitRoom = WaitRoom.of(new CreateWaitRoomDto(hostId, "game", 7), "roomName");
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(waitRoom)); // 호스트 정보 저장

        waitRoom.joinMembers(devUser1Id);
        waitRoomRedisRepository.save(waitRoom);
        joinMemberRedisRepository.save(JoinMember.of(devUser1Id, waitRoom.getId())); // devUser1Id 가입 요청 승인


        RequestWaitRoomDto leaveRequest = new RequestWaitRoomDto(devUser1Id, waitRoom.getId());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/wait-service/waitroom/leave")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(new Gson().toJson(leaveRequest))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "RefreshToken")
                        .header("userId", "userId")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("waitroom-leave",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(STRING).description("유저 아이디"),
                                fieldWithPath("roomId").type(STRING).description("방 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("leaveStatus").type(BOOLEAN).description("퇴장 상태")
                        )));

        assertThat(waitRoomRedisRepository.findById(waitRoom.getId()).orElseThrow(NotFoundWaitRoomException::new).getMembers().size()).isEqualTo(1);
        assertThat(joinMemberRedisRepository.findById(devUser1Id).orElse(null)).isNull();
    }
}