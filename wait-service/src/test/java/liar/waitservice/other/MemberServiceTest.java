package liar.waitservice.other;

import liar.waitservice.other.dao.MemberNameOnly;
import liar.waitservice.wait.MemberDummyInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberServiceTest extends MemberDummyInfo {
    @Autowired
    MemberService memberService;

    String username = "kose";

    @Test
    @DisplayName("회원 찾기를 성공해야 한다")
    public void findMember() throws Exception {
        //given
        MemberNameOnly projectionByUserId = memberService.findUsernameById(hostId);

        //when
        String findName = projectionByUserId.getUsername();

        //then
        Assertions.assertThat(username).isEqualTo(findName);

    }
}