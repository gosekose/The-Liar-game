package liar.game.token.controller.controller;

import com.google.gson.Gson;
import liar.game.member.domain.Authority;
import liar.game.member.domain.Member;
import liar.game.member.repository.AuthorityRepository;
import liar.game.member.repository.MemberRepository;
import liar.game.member.service.AuthorityService;
import liar.game.member.service.MemberService;
import liar.game.member.service.dto.FormRegisterUserDto;
import liar.game.token.controller.dto.LoginDto;
import liar.game.token.controller.dto.TokenAuthDto;
import liar.game.token.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.gosekose.com", uriPort = 443)
@Transactional
@ExtendWith({RestDocumentationExtension.class})
class LoginControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;
    static String accessToken;
    static String refreshToken;

    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired AuthorityService authorityService;
    @Autowired AuthService authService;

//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }


    @Test
    @DisplayName("폼 회원가입 Docs")
    void formRegister() throws Exception {
        // when
        FormRegisterUserDto reqDto = FormRegisterUserDto.builder()
                .username("kose").email("kose1234@naver.com").password("kose123456")
                .build();

        // then
        this.mockMvc.perform(post("/api/v1/register")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(new Gson().toJson(reqDto)))
                .andExpect(status().isOk())
                .andDo(document("register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(STRING).description("회원 이름"),
                                fieldWithPath("email").type(STRING).description("이메일"),
                                fieldWithPath("password").type(STRING).description("패스워드")
                                        .attributes(key("constraint").value("10 ~ 100자"))
                        )));
    }

    @Test
    @DisplayName("폼 로그인 Docs")
    void formLogin() throws Exception {
        // given
        saveMember();

        // when
        LoginDto loginDto = new LoginDto("kose1234@naver.com", "kose123456");

//        // then
        this.mockMvc.perform(
                post("/api/v1/login")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(new Gson().toJson(loginDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("formLogin",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("password").type(STRING).description("패스워드")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken").type(STRING).description("액세스 토큰"),
                                        fieldWithPath("refreshToken").type(STRING).description("리프래쉬 토큰")
                                )));
    }

    // fieldWithPath("accessToken").type(STRING).description("액세스 토큰").optional() 만약 optional 쓰려면,

    @Test
    @DisplayName("로그아웃 요청 Docs")
    void logout() throws Exception {
        // when
        allocateTokenValue();

        //then
        this.mockMvc.perform(
                post("/api/v1/logout")
                        .accept("*/*")
                        .header("Authorization", accessToken)
                        .header("RefreshToken", refreshToken))
                .andExpect(status().isOk())
                .andDo(document("logout"));
    }

    @Test
    @DisplayName("리프래쉬 토큰을 재발급 Docs")
    void reissueToken() throws Exception{
        // when
        allocateTokenValue();

        // then
        this.mockMvc.perform(
                post("/api/v1/reissue")
                        .accept(APPLICATION_JSON)
                        .header("RefreshToken", refreshToken))
                .andExpect(status().isOk())
                .andDo(document("reissue"));
    }


    /**
     * Member 저장
     */
    private void saveMember() {
        memberService.registerForm(FormRegisterUserDto.builder().email("kose1234@naver.com").username("kose").password("kose123456").build(), passwordEncoder);
    }

    /**
     * AuthToken 생성기 (AccessToken, RefreshToken)
     */
    private TokenAuthDto getAuthTokenDto() {
        saveMember();
        Member member = memberService.findByEmail("k@naver.com");
        List<Authority> authorities = authorityService.findAuthorityByUser(member);

        return authService.createFormTokenAuth("k@naver.com", authorities);
    }

    /**
     * Token 할당
     * accessToken, refreshToken 둘 중 하나라도 null인 경우
     */
    private void allocateTokenValue() {
        TokenAuthDto authTokenDto = getAuthTokenDto();
        accessToken = "Bearer " + authTokenDto.getAccessToken();
        refreshToken = authTokenDto.getRefreshToken();
    }


}