package liar.game.token.controller.controller;

import com.google.gson.Gson;
import liar.game.member.service.dto.FormRegisterUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
class LoginControllerDocsTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }


    @Test
    @DisplayName("폼 회원가입 docs 문서화")
    void formRegister() throws Exception {
        //given
        FormRegisterUserDto reqDto = FormRegisterUserDto.builder()
                .username("kose").email("kose1234@naver.com").password("kose123456")
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/register")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(new Gson().toJson(reqDto)))
                .andExpect(status().isOk())
                .andDo(document("register"));
        //when

        //then
    }

    @Test
    void logout() {
    }

    @Test
    void login() {
    }

    @Test
    void reissueToken() {
    }


}