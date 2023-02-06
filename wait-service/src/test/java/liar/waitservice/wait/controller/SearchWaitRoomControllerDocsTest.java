package liar.waitservice.wait.controller;

import com.google.gson.Gson;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.SearchWaitRoomDto;
import liar.waitservice.wait.controller.dto.SearchWaitRoomSliceDto;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.utils.SearchType;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchWaitRoomControllerDocsTest extends CommonController {

    @Test
    @DisplayName("roomName으로 WaitRoom 검색")
    public void searchByRoomName() throws Exception {
        //given
        for (int i = 0; i < 2; i++) {
            waitRoomRedisRepository.save(
                    WaitRoom.of(new CreateWaitRoomDto(
                            "user" + i, "koseRoomName", 7), "koseUsername"));
        }
        SearchWaitRoomDto searchWaitRoomDto = new SearchWaitRoomDto("koseRoomName", SearchType.WAITROOMNAME.getTypeName());

        //when
        ResultActions result = mockMvc.perform(
                get("/wait-service/waitroom/search")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(new Gson().toJson(searchWaitRoomDto))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", "user-id"));


        //then
        result.andExpect(status().isOk())
                .andDo(document("waitroom-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("body").type(STRING).description("검색 내용"),
                                fieldWithPath("searchType").type(STRING).description("검색 조건")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("body[].roomId").type(STRING).description("대기방 Id"),
                                fieldWithPath("body[].roomName").type(STRING).description("대기방 이름"),
                                fieldWithPath("body[].hostId").type(STRING).description("방장 유저 아이디"),
                                fieldWithPath("body[].hostName").type(STRING).description("방장 이름"),
                                fieldWithPath("body[].limitsMembers").type(NUMBER).description("최대 유저 수"),
                                fieldWithPath("body[].joinMembersCnt").type(NUMBER).description("현재 유저 수")
                        )));
    }

    @Test
    @DisplayName("roomName으로 WaitRoom 검색")
    public void searchBySliceRoomName() throws Exception {
        //given
        for (int i = 0; i < 11; i++) {
            waitRoomRedisRepository.save(
                    WaitRoom.of(new CreateWaitRoomDto(
                            "user" + i, "koseRoomName", 7), "koseUsername"));
        }
        SearchWaitRoomDto searchWaitRoomDto = new SearchWaitRoomSliceDto("koseRoomName", SearchType.WAITROOMNAME.getTypeName(), 0, 10);

        //when
        ResultActions result = mockMvc.perform(
                get("/wait-service/waitroom-slice/search")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(new Gson().toJson(searchWaitRoomDto))
                        .header("Authorization", "Bearer AccessToken")
                        .header("RefreshToken", "refreshToken")
                        .header("userId", "user-id"));


        //then
        result.andExpect(status().isOk())
                .andDo(document("waitroom-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("body").type(STRING).description("검색 내용"),
                                fieldWithPath("searchType").type(STRING).description("검색 조건"),
                                fieldWithPath("page").type(NUMBER).description("페이지 번호"),
                                fieldWithPath("limit").type(NUMBER).description("페이지 당 데이터 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("body.content[].roomId").type(STRING).description("대기방 Id"),
                                fieldWithPath("body.content[].roomName").type(STRING).description("대기방 이름"),
                                fieldWithPath("body.content[].hostId").type(STRING).description("방장 유저 아이디"),
                                fieldWithPath("body.content[].hostName").type(STRING).description("방장 이름"),
                                fieldWithPath("body.content[].limitsMembers").type(NUMBER).description("최대 유저 수"),
                                fieldWithPath("body.content[].joinMembersCnt").type(NUMBER).description("현재 유저 수"),
                                fieldWithPath("body.pageable.sort.empty").type(BOOLEAN).description("pageable.sort.empty"),
                                fieldWithPath("body.pageable.sort.sorted").type(BOOLEAN).description("pageable.sort.sorted"),
                                fieldWithPath("body.pageable.sort.unsorted").type(BOOLEAN).description("pageable.sort.unsorted"),
                                fieldWithPath("body.pageable.offset").type(NUMBER).description("pageable.offset"),
                                fieldWithPath("body.pageable.pageSize").type(NUMBER).description("pageable.pageSize"),
                                fieldWithPath("body.pageable.pageNumber").type(NUMBER).description("pageable.pageNumber"),
                                fieldWithPath("body.pageable.paged").type(BOOLEAN).description("pageable.paged"),
                                fieldWithPath("body.pageable.unpaged").type(BOOLEAN).description("pageable.unpaged"),
                                fieldWithPath("body.totalPages").type(NUMBER).description("totalPages"),
                                fieldWithPath("body.totalElements").type(NUMBER).description("totalElements"),
                                fieldWithPath("body.last").type(BOOLEAN).description("last"),
                                fieldWithPath("body.size").type(NUMBER).description("size"),
                                fieldWithPath("body.number").type(NUMBER).description("number"),
                                fieldWithPath("body.sort.empty").type(BOOLEAN).description("sort.empty"),
                                fieldWithPath("body.sort.unsorted").type(BOOLEAN).description("sort.unsorted"),
                                fieldWithPath("body.sort.sorted").type(BOOLEAN).description("sort.sorted"),
                                fieldWithPath("body.numberOfElements").type(NUMBER).description("numberOfElements"),
                                fieldWithPath("body.first").type(BOOLEAN).description("first"),
                                fieldWithPath("body.empty").type(BOOLEAN).description("empty")
                        )));
    }
}