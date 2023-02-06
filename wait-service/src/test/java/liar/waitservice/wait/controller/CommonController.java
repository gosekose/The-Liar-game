package liar.waitservice.wait.controller;

import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com", uriPort = 443)
@Transactional
@ExtendWith(RestDocumentationExtension.class)
public class CommonController {

    protected MockMvc mockMvc;
    protected String hostId = "159b49cd-78d2-4b2d-8aa2-5b986b623251";
    protected String devUser1Id = "fe557c5a-15c2-41f5-83a4-c87d8061c45a";
    protected String devUser2Id = "2b970366-7a85-41da-b636-628969c45e88";
    protected String devUser3Id = "0fa66a79-f4cc-4bf4-8a3e-f2cd81cf82c5";
    protected String devUser4Id = "b2886e51-6444-4b1b-94e0-a34ab1dbbb3b";
    protected String devUser5Id = "2d972b43-4d2d-4383-b1aa-32fabba42185";
    protected String devUser6Id = "a02814cf-d529-43a4-b2d9-941b35b3b8fa";

    @Autowired
    WaitRoomRedisRepository waitRoomRedisRepository;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @AfterEach
    public void tearDown() {
        waitRoomRedisRepository.deleteAll();
    }

}
