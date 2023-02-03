package liar.waitservice.testconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liar.waitservice.wait.controller.SearchWaitRoomController;
import liar.waitservice.wait.repository.redis.WaitRoomRedisRepository;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


/**
 * author: backtony
 * link: https://url.kr/cyhs8p
 */
@Disabled
@WebMvcTest({
//        UpdateWaitRoomStatusController.class,
        SearchWaitRoomController.class
})
public abstract class ControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired protected MockMvc mockMvc;

    @MockBean
    protected WaitRoomRedisRepository waitRoomRedisRepository;

    protected String createJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}