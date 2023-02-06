package liar.waitservice.wait.repository.redis;

import liar.waitservice.wait.domain.JoinMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JoinMemberRedisRepositoryTest {

    @Autowired
    JoinMemberRedisRepository joinMemberRedisRepository;

//    @Test
//    @DisplayName("Test")
//    public void test() throws Exception {
//        //given
//
//        JoinMember test = joinMemberRedisRepository.findJoinMemberById("test");
//
//        assertThat(test).isNull();;
//        //when
//
//        //then
//
//    }

}