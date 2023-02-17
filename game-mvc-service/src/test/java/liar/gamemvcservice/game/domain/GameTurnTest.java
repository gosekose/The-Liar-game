package liar.gamemvcservice.game.domain;

import liar.gamemvcservice.exception.exception.NotEqualUserIdException;
import liar.gamemvcservice.exception.exception.NotUserTurnException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GameTurnTest {

    @Test
    @DisplayName("GameTurn save")
    public void save() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("1", "2", "3", "4", "5");
        Collections.shuffle(userIds);

        for (String userId : userIds) {
            System.out.println("userId = " + userId);
        }

        //when
        GameTurn gameTurn = new GameTurn(gameId, userIds);

        //then
        assertThat(gameTurn.getGameId()).isEqualTo(gameId);
        assertThat(gameTurn.getPlayerTurnsConsistingOfUserId().size()).isEqualTo(5);

    }

    @Test
    @DisplayName("플레이어 턴이 맞는지 확인하는 메소드 (private)")
    public void isPlayerTurn_private() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("1", "2", "3", "4", "5");
        GameTurn gameTurn = new GameTurn(gameId, userIds);

        //when
        int idx = gameTurn.getNowTurn() % gameTurn.getPlayerTurnsConsistingOfUserId().size();

        boolean[] results = new boolean[5];

        for (int i = 0; i < results.length; i++) {
            if (gameTurn.getPlayerTurnsConsistingOfUserId().get(idx + i).equals("1")) {
                results[i] = true;
            } else {
                results[i] = false;
            }
        }

        //then
        assertThat(idx).isEqualTo(0);
        assertThat(results[0]).isTrue();
        assertThat(results[1]).isFalse();
        assertThat(results[2]).isFalse();
        assertThat(results[3]).isFalse();
        assertThat(results[4]).isFalse();
    }

    @Test
    @DisplayName("플레이어의 턴이 맞다면, 턴을 증가시킨다.")
    public void updateTurnCntWhenPlayerTurnIsValidated() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("0", "1", "2", "3", "4");
        GameTurn gameTurn = new GameTurn(gameId, userIds);

        //when
        for (int i = 0; i < userIds.size(); i++) {
            gameTurn.updateTurnCntWhenPlayerTurnIsValidated(String.valueOf(i));
        }

        //then
        assertThat(gameTurn.getNowTurn()).isEqualTo(5);
    }

    @Test
    @DisplayName("플레이어의 턴이 아니라면, 예외를 던지고 cnt를 추가하지 않는다.")
    public void updateTurnCntWhenPlayerTurnIsValidated_exception() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("0", "1", "2", "3", "4");
        GameTurn gameTurn = new GameTurn(gameId, userIds);

        //when
        String userId = "3";

        //then
        Assertions.assertThatThrownBy(
                () -> {gameTurn.updateTurnCntWhenPlayerTurnIsValidated(userId);}
        ).isInstanceOf(NotUserTurnException.class);
        assertThat(gameTurn.getNowTurn()).isEqualTo(0);
    }

    @Test
    @DisplayName("마지막 턴이 아니라면 userId, false, 마지막턴 이라면 null, true를 출력한다.")
    public void setIfExistsNextTurn() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("0", "1", "2", "3", "4");
        GameTurn gameTurn = new GameTurn(gameId, userIds);

        //when
        List<NextTurn> nextTurns = new ArrayList<>();

        int turn;
        for (int i = 0; i < userIds.size() * 2; i++) {

            turn = i % userIds.size();

            gameTurn.updateTurnCntWhenPlayerTurnIsValidated(String.valueOf(turn));
            nextTurns.add(gameTurn.setIfExistsNextTurn());
        }

        int idx;

        //then
        for (int i = 0; i < userIds.size() * 2; i++) {


            if (i == userIds.size() * 2 - 1) {
                assertThat(nextTurns.get(i).getUserIdOfNextTurn()).isNull();
                assertThat(nextTurns.get(i).isLastTurn()).isTrue();
            } else {

                idx = (i + 1) % userIds.size();
                assertThat(nextTurns.get(i).getUserIdOfNextTurn()).isEqualTo(String.valueOf(idx));
                assertThat(nextTurns.get(i).isLastTurn()).isFalse();
            }

        }
    }

}