package liar.resultservice.result.domain;

import liar.resultservice.other.member.Member;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.result.MemberDummyInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class PlayerTest extends MemberDummyInfo {

    @Autowired
    MemberRepository memberRepository;

    private Member member;


    @BeforeEach
    public void init() {
        member = memberRepository.findByUserId(devUser1Id);
    }

    @Test
    @DisplayName("update Level")
    public void updateLevel() throws Exception {
        //given
        Player player = Player.of(member);

        //when
        player.levelUp(Level.BRONZE2);
        player.updateExp(1000L);
        player.updateGameResult(true);
        player.updateVisibleGameResult();

        //then
        assertThat(player.getLevel()).isEqualTo(Level.BRONZE2);
        assertThat(player.getExp()).isEqualTo(1000L);
        assertThat(player.getWins()).isEqualTo(1L);
        assertThat(player.getTotalGames()).isEqualTo(1L);
        assertThat(player.isVisibleGameResult()).isFalse();
    }
}