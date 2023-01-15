//package liar.game.business.game.service;
//
//import liar.game.business.game.domain.Game;
//import liar.game.business.game.domain.GameResult;
//import liar.game.business.game.domain.Result;
//import liar.game.business.game.repository.dto.AllMemberGameResultAnalysisInterface;
//import liar.game.business.game.repository.dto.AllMemberGameResultAnalysisJpqlInterface;
//import liar.game.member.domain.Member;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class GameResultServiceTest {
//
//    @Autowired
//    EntityManager em;
//
//    @Autowired
//    GameResultService gameResultService;
//
//    static Member[] members = new Member[5];
//
//    @BeforeEach
//    @Transactional
//    public void init() {
//
//        for (int i = 0; i < 5; i++) {
//
//            String uuid = UUID.randomUUID().toString();
//
//            Member member = Member.builder()
//                    .email(i + "@naver.com")
//                    .registrationId("NAVER")
//                    .authorities("ROLE_USER")
//                    .password(UUID.randomUUID().toString())
//                    .registerId(uuid)
//                    .build();
//
//            em.persist(member);
//            members[i] = member;
//        }
//
//        for (int i = 0; i < 5; i++) {
//
//            Game game = Game.of(members[i]);
//
//            em.persist(game);
//
//            for (int j = 0; j < 5; j++) {
//
//                Result result;
//
//                if (j != i) result = Result.LOSE;
//                else result = Result.WIN;
//
//                GameResult gameResult = GameResult
//                        .builder()
//                        .member(members[j])
//                        .game(game)
//                        .result(result)
//                        .build();
//
//                em.persist(gameResult);
//            }
//        }
//
//        em.flush();
//    }
//
//    @Test
//    @DisplayName("각 회원의 이긴 횟수, 진 횟수, 승리 확률을 페이징하여 가져 온다.")
//    public void fetchAllMemberGameResultAnalysis() throws Exception {
//        //given
//        Pageable page = PageRequest.of(0, 10);
//
//        //when
//        Page<AllMemberGameResultAnalysisInterface> result = gameResultService.fetchAllMembersGameResultAnalysis(page);
//
//        //then
//        assertThat(result.getContent().size()).isEqualTo(10);
//        assertThat(result.getContent().get(0).getWin()).isEqualTo(1L);
//        assertThat(result.getContent().get(1).getLose()).isEqualTo(4L);
//        assertThat(result.getContent().get(2).getLose()).isEqualTo(4L);
//        assertThat(result.getContent().get(3).getLose()).isEqualTo(4L);
//        assertThat(result.getContent().get(4).getLose()).isEqualTo(4L);
//        assertThat(result.getContent().get(2).getRate()).isEqualTo(20.0);
//        assertThat(result.getContent().get(2).getLastjointime()).isBefore(LocalDateTime.now());
//
//    }
//
//    @Test
//    @DisplayName("각 회원의 이긴 횟수, 진 횟수, 승리 확률을 페이징하여 가져 온다.(JPQL)")
//    public void fetchAllMemberGameResultAnalysisJpql() throws Exception {
//        //given
//        Pageable page = PageRequest.of(0, 10);
//
//        //when
//        Page<AllMemberGameResultAnalysisJpqlInterface> result = gameResultService.fetchAllMembersGameResultAnalysisJpql(page);
//
//        //then
//        assertThat(result.getContent().size()).isEqualTo(10);
//        assertThat(result.getContent().get(0).getWin()).isEqualTo(1L);
//        assertThat(result.getContent().get(1).getLose()).isEqualTo(4L);
//        assertThat(result.getContent().get(2).getLose()).isEqualTo(4L);
//        assertThat(result.getContent().get(3).getLose()).isEqualTo(4L);
//        assertThat(result.getContent().get(4).getLose()).isEqualTo(4L);
//        assertThat(result.getContent().get(2).getRate()).isEqualTo(20.0);
//        assertThat(result.getContent().get(2).getLastJoinGameTime()).isBefore(LocalDateTime.now());
//
//    }
//
//}