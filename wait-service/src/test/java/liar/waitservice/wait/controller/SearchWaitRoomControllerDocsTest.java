package liar.waitservice.wait.controller;

import com.google.gson.Gson;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.SearchWaitRoomDto;
import liar.waitservice.wait.domain.utils.SearchType;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static javax.management.openmbean.SimpleType.INTEGER;
import static javax.management.openmbean.SimpleType.STRING;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.liargame.com", uriPort = 443)
@Transactional
@ExtendWith({RestDocumentationExtension.class})
class SearchWaitRoomControllerDocsTest {

//    @Mock SearchWaitRoomController controller;

    @Mock
    private MockMvc mockMvc;

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        for (int i = 0; i < 15; i++) {
            waitRoomRedisRepository.save(
                    WaitRoom.of(new CreateWaitRoomDto(
                            "user" + i, "koseRoomName", 7), "koseUsername"));
        }
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("roomName으로 WaitRoom 검색")
    public void searchByRoomName() throws Exception {
        //given
        SearchWaitRoomDto searchWaitRoomDto = new SearchWaitRoomDto("koseRoomName", SearchType.WAITROOMNAME.getTypeName());

        //when
        mockMvc.perform(
                        post("/wait-service/search/waitroom")
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(new Gson().toJson(searchWaitRoomDto)))
                .andExpect(status().isOk())
                .andDo(document("waitroom_search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("body").type(STRING).description("검색 내용"),
                                fieldWithPath("searchType").type(STRING).description("검색 조건")
                        ),
                        responseFields(
                                fieldWithPath("code").type(STRING).description("결과 코드"),
                                fieldWithPath("message").type(STRING).description("결과 메세지"),
                                fieldWithPath("body.roomId").type(STRING).description("대기방 Id"),
                                fieldWithPath("body.roomName").type(STRING).description("대기방 이름"),
                                fieldWithPath("body.hostId").type(STRING).description("방장 유저 아이디"),
                                fieldWithPath("body.hostName").type(STRING).description("방장 이름"),
                                fieldWithPath("body.limitsMembers").type(INTEGER).description("최대 유저 수"),
                                fieldWithPath("body.joinMembersCnt").type(INTEGER).description("현재 유저 수")
                        )));

    }

}