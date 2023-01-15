//package liar.game;
//
//import liar.game.business.game.domain.Game;
//import liar.game.business.game.domain.GameResult;
//import liar.game.business.game.domain.Result;
//import liar.game.member.domain.Member;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.EntityManager;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class InitDb {
//
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.dbInit();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//
//        private final EntityManager em;
//
//        static Member[] members = new Member[5];
//
//        public void dbInit() {
//
//            for (int i = 0; i < 5; i++) {
//
//                String uuid = UUID.randomUUID().toString();
//
//                Member member = Member.builder()
//                        .email(i + "@naver.com")
//                        .registrationId("NAVER")
//                        .authorities("ROLE_USER")
//                        .password(UUID.randomUUID().toString())
//                        .registerId(uuid)
//                        .build();
//
//                em.persist(member);
//                members[i] = member;
//            }
//
//            for (int i = 0; i < 5; i++) {
//
//                Game game = Game.of(members[i]);
//
//                em.persist(game);
//
//                for (int j = 0; j < 5; j++) {
//
//                    Result result;
//
//                    if (j != i) result = Result.LOSE;
//                    else result = Result.WIN;
//
//                    GameResult gameResult = GameResult
//                            .builder()
//                            .member(members[j])
//                            .game(game)
//                            .result(result)
//                            .build();
//
//                    em.persist(gameResult);
//                }
//            }
//
//            em.flush();
//        }
//
//    }
//
//}
