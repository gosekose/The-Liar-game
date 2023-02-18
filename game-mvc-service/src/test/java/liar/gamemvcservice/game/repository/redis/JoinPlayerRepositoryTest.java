package liar.gamemvcservice.game.repository.redis;

import liar.gamemvcservice.game.domain.GameRole;
import liar.gamemvcservice.game.domain.JoinPlayer;
import liar.gamemvcservice.game.domain.Player;
import liar.gamemvcservice.game.repository.redis.JoinPlayerRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static liar.gamemvcservice.game.domain.GameRole.CITIZEN;
import static liar.gamemvcservice.game.domain.GameRole.LIAR;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JoinPlayerRepositoryTest {

    @Autowired
    JoinPlayerRepository joinPlayerRepository;

    private int num;
    private Thread[] threads;
    private String gameId1;
    private String gameId2;

    @BeforeEach
    public void init() {
        num = 5;
        threads = new Thread[num];
        gameId1 = "1";
        gameId2 = "2";
    }

    @AfterEach
    public void tearDown() {
        joinPlayerRepository.deleteAll();
    }


    @Test
    @DisplayName("joinPlayer를 저장한다.")
    public void save() throws Exception {
        //given
        List<JoinPlayer> joinPlayers = getJoinPlayers();

        //when
        List<JoinPlayer> savedJoinPlayers = new ArrayList<>();
        for (int i = 0; i < joinPlayers.size(); i++) {
            savedJoinPlayers.add(joinPlayerRepository.save(joinPlayers.get(i)));
        }

        //then
        for (int i = 0; i < savedJoinPlayers.size(); i++) {
            assertThat(savedJoinPlayers.get(i).getId()).isEqualTo(joinPlayers.get(i).getId());
            assertThat(savedJoinPlayers.get(i).getGameId()).isEqualTo(joinPlayers.get(i).getGameId());
            assertThat(savedJoinPlayers.get(i).getPlayer().getUserId()).isEqualTo(joinPlayers.get(i).getPlayer().getUserId());
            assertThat(savedJoinPlayers.get(i).getPlayer().getGameRole()).isEqualTo(joinPlayers.get(i).getPlayer().getGameRole());
        }
    }

    @Test
    @DisplayName("joinPlayer를 gameId 인덱스로 검색한다. gameId가 같은 joinPlayer의 리스트를 반환한다.")
    public void find_index() throws Exception {
        //given
        saveJoinPlayers();

        //when
        List<JoinPlayer> findJoinPlayers = joinPlayerRepository.findByGameId(gameId1);

        //then
        assertThat(findJoinPlayers.size()).isEqualTo(4);
        assertThat(findJoinPlayers.get(0).getGameId()).isEqualTo(gameId1);

    }

    @Test
    @DisplayName("save")
    public void save_multiThread() throws Exception {
        //given
        List<JoinPlayer> joinPlayers = getJoinPlayers();

        //when
        for (int i = 0; i < num; i++) {
            int finalIdx = i;
            threads[i] = new Thread(() -> {
                joinPlayerRepository.save(joinPlayers.get(finalIdx));
            });
        }

        runThread();

        List<JoinPlayer> joinPlayerGameId1 = joinPlayerRepository.findByGameId("1");
        List<JoinPlayer> joinPlayerGameId2 = joinPlayerRepository.findByGameId("2");
        List<JoinPlayer> citizensGameId1 = joinPlayerGameId1.stream()
                .filter(player -> player.getPlayer().getGameRole() == CITIZEN)
                .collect(Collectors.toList());

        //then
        assertThat(joinPlayerGameId1.size()).isEqualTo(4);
        assertThat(citizensGameId1.size()).isEqualTo(3);
        assertThat(joinPlayerGameId2.size()).isEqualTo(1);
    }

    @NotNull
    private List<JoinPlayer> getJoinPlayers() {
        List<JoinPlayer> joinPlayers = new ArrayList<>();
        joinPlayers.add(new JoinPlayer(gameId1, new Player("1", CITIZEN)));
        joinPlayers.add(new JoinPlayer(gameId1, new Player("2", CITIZEN)));
        joinPlayers.add(new JoinPlayer(gameId1, new Player("3", CITIZEN)));
        joinPlayers.add(new JoinPlayer(gameId1, new Player("4", LIAR)));
        joinPlayers.add(new JoinPlayer(gameId2, new Player("4", LIAR)));
        return joinPlayers;
    }

    private void saveJoinPlayers() {
        joinPlayerRepository.save(new JoinPlayer(gameId1, new Player("1", CITIZEN)));
        joinPlayerRepository.save(new JoinPlayer(gameId1, new Player("2", CITIZEN)));
        joinPlayerRepository.save(new JoinPlayer(gameId1, new Player("3", CITIZEN)));
        joinPlayerRepository.save(new JoinPlayer(gameId1, new Player("4", LIAR)));
        joinPlayerRepository.save(new JoinPlayer(gameId2, new Player("4", LIAR)));
    }

    private void runThread() throws InterruptedException {
        for (int i = 0; i < num; i++) threads[i].start();
        for (int i = 0; i < num; i++) threads[i].join();
    }


}