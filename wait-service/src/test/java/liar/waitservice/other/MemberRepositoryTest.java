package liar.waitservice.other;

import liar.waitservice.other.dao.MemberNameOnly;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    String userId = "159b49cd-78d2-4b2d-8aa2-5b986b623251";
    String username = "kose";

    @Test
    @DisplayName("회원 찾기를 성공해야 한다")
    public void findMember() throws Exception {
        //given
        MemberNameOnly projectionByUserId = memberRepository.findProjectionByUserId(userId);

        //when
        String findName = projectionByUserId.getUsername();

        //then
        Assertions.assertThat(username).isEqualTo(findName);

    }


}