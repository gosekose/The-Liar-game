package liar.resultservice.result.controller;

import jakarta.transaction.Transactional;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.MemberDummyInfo;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.sql.JDBCType.BOOLEAN;
import static javax.swing.text.html.parser.DTDConstants.NUMBER;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com", uriPort = 443)
@Transactional
@ExtendWith(RestDocumentationExtension.class)
public class CommonController extends MemberDummyInfo {

    protected MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameResultRepository gameResultRepository;
    @Autowired
    PlayerResultRepository playerResultRepository;
    @Autowired
    TopicRepository topicRepository;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();

    }


    public ResponseFieldsSnippet responseFieldsSnippetPageable(FieldDescriptor... fieldDescriptors) {
        FieldDescriptor[] fields = new FieldDescriptor[] {
                fieldWithPath("body.pageable.offset").type(NUMBER).description("The offset of the current page"),
                fieldWithPath("body.pageable.pageNumber").type(NUMBER).description("The number of the current page"),
                fieldWithPath("body.pageable.pageSize").type(NUMBER).description("The size of the current page"),
                fieldWithPath("body.pageable.paged").type(BOOLEAN).description("Whether the current page is paged"),
                fieldWithPath("body.pageable.unpaged").type(BOOLEAN).description("Whether the current page is unpaged"),
                fieldWithPath("body.sort.empty").type(BOOLEAN).description("Whether the current page is sorted"),
                fieldWithPath("body.sort.sorted").type(BOOLEAN).description("Whether the current page is sorted"),
                fieldWithPath("body.sort.unsorted").type(BOOLEAN).description("Whether the current page is sorted"),
                fieldWithPath("body.pageable.sort.empty").type(BOOLEAN).description("Whether the current page is sorted"),
                fieldWithPath("body.pageable.sort.sorted").type(BOOLEAN).description("Whether the current page is sorted"),
                fieldWithPath("body.pageable.sort.unsorted").type(BOOLEAN).description("Whether the current page is sorted"),
                fieldWithPath("body.totalPages").type(NUMBER).description("The total number of pages"),
                fieldWithPath("body.totalElements").type(NUMBER).description("The total number of elements"),
                fieldWithPath("body.last").type(BOOLEAN).description("Whether the current page is the last one"),
                fieldWithPath("body.size").type(NUMBER).description("The size of the current page"),
                fieldWithPath("body.number").type(NUMBER).description("The number of the current page"),
                fieldWithPath("body.numberOfElements").type(NUMBER).description("The number of elements in the current page"),
                fieldWithPath("body.first").type(BOOLEAN).description("Whether the current page is the first one"),
                fieldWithPath("body.empty").type(BOOLEAN).description("Whether the current page is empty")
        };
        return responseFields(fieldDescriptors).and(fields);
    }
}
