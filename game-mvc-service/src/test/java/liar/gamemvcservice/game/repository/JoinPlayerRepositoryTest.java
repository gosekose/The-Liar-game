package liar.gamemvcservice.game.repository;

import liar.gamemvcservice.game.domain.GameRole;
import liar.gamemvcservice.game.domain.JoinPlayer;
import liar.gamemvcservice.game.domain.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JoinPlayerRepositoryTest {

    @Autowired
    JoinPlayerRepository joinPlayerRepository;

    @AfterEach
    public void tearDown() {
        joinPlayerRepository.deleteAll();
    }

    @Test
    @DisplayName("joinPlayer를 저장한다.")
    public void save() throws Exception {
        //given
        JoinPlayer jp1 = new JoinPlayer("1", new Player("1", GameRole.CITIZEN));
        JoinPlayer jp2 = new JoinPlayer("1", new Player("2", GameRole.CITIZEN));
        JoinPlayer jp3 = new JoinPlayer("1", new Player("3", GameRole.CITIZEN));
        JoinPlayer jp4 = new JoinPlayer("1", new Player("4", GameRole.LIAR));

        //when
        JoinPlayer savedJp1 = joinPlayerRepository.save(jp1);
        JoinPlayer savedJp2 = joinPlayerRepository.save(jp2);
        JoinPlayer savedJp3 = joinPlayerRepository.save(jp3);
        JoinPlayer savedJp4 = joinPlayerRepository.save(jp4);

        //then
        assertThat(savedJp1.getId()).isEqualTo(jp1.getId());
        assertThat(savedJp1.getGameId()).isEqualTo(jp1.getGameId());
        assertThat(savedJp1.getPlayer().getUserId()).isEqualTo(jp1.getPlayer().getUserId());
        assertThat(savedJp1.getPlayer().getGameRole()).isEqualTo(jp1.getPlayer().getGameRole());

        assertThat(savedJp2.getId()).isEqualTo(jp2.getId());
        assertThat(savedJp2.getGameId()).isEqualTo(jp2.getGameId());
        assertThat(savedJp2.getPlayer().getUserId()).isEqualTo(jp2.getPlayer().getUserId());
        assertThat(savedJp2.getPlayer().getGameRole()).isEqualTo(jp2.getPlayer().getGameRole());

        assertThat(savedJp3.getId()).isEqualTo(jp3.getId());
        assertThat(savedJp3.getGameId()).isEqualTo(jp3.getGameId());
        assertThat(savedJp3.getPlayer().getUserId()).isEqualTo(jp3.getPlayer().getUserId());
        assertThat(savedJp3.getPlayer().getGameRole()).isEqualTo(jp3.getPlayer().getGameRole());

        assertThat(savedJp4.getId()).isEqualTo(jp4.getId());
        assertThat(savedJp4.getGameId()).isEqualTo(jp4.getGameId());
        assertThat(savedJp4.getPlayer().getUserId()).isEqualTo(jp4.getPlayer().getUserId());
        assertThat(savedJp4.getPlayer().getGameRole()).isEqualTo(jp4.getPlayer().getGameRole());
    }

    @Test
    @DisplayName("joinPlayer를 gameId 인덱스로 검색한다. gameId가 같은 joinPlayer의 리스트를 반환한다.")
    public void find_index() throws Exception {
        //given
        JoinPlayer jp1 = new JoinPlayer("1", new Player("1", GameRole.CITIZEN));
        JoinPlayer jp2 = new JoinPlayer("1", new Player("2", GameRole.CITIZEN));
        JoinPlayer jp3 = new JoinPlayer("1", new Player("3", GameRole.CITIZEN));
        JoinPlayer jp4 = new JoinPlayer("1", new Player("4", GameRole.LIAR));
        JoinPlayer jp5 = new JoinPlayer("2", new Player("4", GameRole.LIAR));

        JoinPlayer savedJp1 = joinPlayerRepository.save(jp1);
        JoinPlayer savedJp2 = joinPlayerRepository.save(jp2);
        JoinPlayer savedJp3 = joinPlayerRepository.save(jp3);
        JoinPlayer savedJp4 = joinPlayerRepository.save(jp4);

        JoinPlayer savedJp5 = joinPlayerRepository.save(jp5);

        //when
        List<JoinPlayer> findJoinPlayers = joinPlayerRepository.findByGameId("1");

        //then
        assertThat(findJoinPlayers.size()).isEqualTo(4);
        assertThat(findJoinPlayers.get(0).getGameId()).isEqualTo("1");

    }

}