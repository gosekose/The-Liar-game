package liar.resultservice.result.service.ranking;

import jakarta.transaction.Transactional;
import liar.resultservice.other.member.Member;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.result.MemberDummyInfo;
import liar.resultservice.result.domain.Level;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RankingPolicyTest extends MemberDummyInfo {

    @Autowired
    RankingPolicy rankingPolicy;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    MemberRepository memberRepository;
    List<String> users = Arrays.asList(hostId, devUser1Id, devUser2Id, devUser3Id, devUser4Id, devUser5Id, devUser6Id);

    @BeforeEach
    public void init() {
        for (int i = 0; i < users.size(); i++) {
            Long wins = i * 10L;
            Long loses = i * 5L;
            Long exp = i * 100 + 30L;

            Level playerLevel = null;
            for (Level level : Level.values()) {
                if (exp < level.getMaxExp()) {
                    playerLevel = level;
                }
            }
            if (playerLevel == null) playerLevel = Level.LIAR;

            Member member = memberRepository.findByUserId(users.get(i));
            playerRepository.save(new Player(member, wins, loses, wins + loses
                    , exp, playerLevel));
        }

    }

    @AfterEach
    public void tearDown() {
        playerRepository.deleteAll();
    }

    @Test
    @DisplayName("플레이어의 랭킹을 받아 값을 리턴한다.")
    public void fetchPlayerRanking() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 5);

        //when
        Slice<PlayerRankingDto> playerRankingDtos = rankingPolicy.fetchPlayerRanking(page);

        //then
        assertThat(playerRankingDtos.getSize()).isEqualTo(5);
        assertThat(playerRankingDtos.getContent().size()).isEqualTo(5);
        assertThat(playerRankingDtos.getContent().get(0).getExp()).isEqualTo(630L);
    }
}