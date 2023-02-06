package liar.gameservice.other;

import liar.gameservice.MemberDummyInfo;
import liar.gameservice.exception.exception.NotFoundUserException;
import liar.gameservice.other.dao.MemberIdOnly;
import liar.gameservice.other.dao.MemberNameOnly;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest extends MemberDummyInfo {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("username으로 userId 찾기 성공")
    public void findByUsername_Success() throws Exception {
        //given when
        Flux<MemberIdOnly> result = memberService.findByUsername(username);

        //then
        StepVerifier
                .create(result)
                .assertNext(m -> assertThat(m.getUserId()).isEqualTo(userId))
                .verifyComplete();
    }

    @Test
    @DisplayName("username으로 userId 찾기 실패")
    public void findByUsername_Fail() throws Exception {
        //given when
        Flux<MemberIdOnly> result = memberService.findByUsername("notFound");

        //then
        StepVerifier
                .create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("userId로 member UserName 찾기 성공")
    public void findByUserId_Success() throws Exception {
        //given - when
        Mono<MemberNameOnly> result = memberService.findUsernameById(userId);

        //then
        StepVerifier
                .create(result)
                .assertNext(m -> assertThat(m.getUsername()).isEqualTo(username))
                .verifyComplete();
    }

    @Test
    @DisplayName("userId로 member Username 찾기 실패")
    public void findByUserId_Fail() throws Exception {
        //given - when
        Mono<MemberNameOnly> result = memberService.findUsernameById("notFound");

        //then
        StepVerifier
                .create(result)
                .expectError(NotFoundUserException.class)
                .verify();
    }

}