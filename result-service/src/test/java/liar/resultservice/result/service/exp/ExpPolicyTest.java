package liar.resultservice.result.service.exp;

import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Level;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static liar.resultservice.result.domain.Level.GOLD2;
import static liar.resultservice.result.domain.Level.LIAR;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ExpPolicyTest {

    @Autowired
    ExpPolicy expPolicy;
    
    @Test
    @DisplayName("calculateExp liar Win")
    public void calculateExp_win_liar() throws Exception {
        //given
        GameRole gameRole = GameRole.LIAR;

        //when
        Long exp1 = expPolicy.calculateExp(gameRole, true, true, 3);
        Long exp2 = expPolicy.calculateExp(gameRole, true, true, 10);
        Long exp3 = expPolicy.calculateExp(gameRole, true, true, 15);
        Long exp4 = expPolicy.calculateExp(gameRole, false, true, 3);
        Long exp5 = expPolicy.calculateExp(gameRole, false, false, 10);
        Long exp6 = expPolicy.calculateExp(gameRole, false, true, 15);

        //then
        assertThat(exp1).isEqualTo(50L);
        assertThat(exp2).isEqualTo(43L);
        assertThat(exp3).isEqualTo(38L);
        assertThat(exp4).isEqualTo(15L);
        assertThat(exp5).isEqualTo(15L);
        assertThat(exp6).isEqualTo(15L);
    }

    @Test
    @DisplayName("calculateExp citizen Win")
    public void calculateExp_win_citizen() throws Exception {
        //given
        GameRole gameRole = GameRole.CITIZEN;

        //when
        Long exp1 = expPolicy.calculateExp(gameRole, true, true, 3);
        Long exp2 = expPolicy.calculateExp(gameRole, true, true, 10);
        Long exp3 = expPolicy.calculateExp(gameRole, true, true, 15);
        Long exp4 = expPolicy.calculateExp(gameRole, true, false, 3);
        Long exp5 = expPolicy.calculateExp(gameRole, true, false, 10);
        Long exp6 = expPolicy.calculateExp(gameRole, false, true, 15);
        Long exp7 = expPolicy.calculateExp(gameRole, false, false, 15);

        //then
        assertThat(exp1).isEqualTo(28L);
        assertThat(exp2).isEqualTo(35L);
        assertThat(exp3).isEqualTo(40L);
        assertThat(exp4).isEqualTo(20L);
        assertThat(exp5).isEqualTo(20L);
        assertThat(exp6).isEqualTo(13L);
        assertThat(exp7).isEqualTo(10L);
    }

    
    @Test
    @DisplayName("레벨을 업데이트 한다.")
    public void nextLevel() throws Exception {
        //given
        Long exp1 = 1240L;
        Long exp2 = 5200L;
        //when
        Level level1 = expPolicy.nextLevel(exp1);
        Level level2 = expPolicy.nextLevel(exp2);

        //then
        assertThat(level1).isEqualTo(GOLD2);
        assertThat(level2).isEqualTo(LIAR);
    }




}