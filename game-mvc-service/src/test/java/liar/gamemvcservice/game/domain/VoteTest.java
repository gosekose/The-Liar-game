package liar.gamemvcservice.game.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class VoteTest {

    @Test
    @DisplayName("Vote를 생성한다.")
    public void saveVote() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("1", "2", "3", "4", "5");

        //when
        Vote vote = new Vote(gameId, userIds);

        //then
        assertThat(vote.getVotedResults().size()).isEqualTo(5);
        assertThat(vote.getVotedResults().get(0).getLiarId()).isEqualTo("1");
        assertThat(vote.getVotedResults().get(0).getUserIds().size()).isEqualTo(0);
        assertThat(vote.getVotedResults().get(0).getCnt()).isEqualTo(0);

        assertThat(vote.getVotedResults().get(1).getLiarId()).isEqualTo("2");
        assertThat(vote.getVotedResults().get(1).getUserIds().size()).isEqualTo(0);
        assertThat(vote.getVotedResults().get(1).getCnt()).isEqualTo(0);

        assertThat(vote.getVotedResults().get(2).getLiarId()).isEqualTo("3");
        assertThat(vote.getVotedResults().get(2).getUserIds().size()).isEqualTo(0);
        assertThat(vote.getVotedResults().get(2).getCnt()).isEqualTo(0);

        assertThat(vote.getVotedResults().get(3).getLiarId()).isEqualTo("4");
        assertThat(vote.getVotedResults().get(3).getUserIds().size()).isEqualTo(0);
        assertThat(vote.getVotedResults().get(3).getCnt()).isEqualTo(0);

        assertThat(vote.getVotedResults().get(4).getLiarId()).isEqualTo("5");
        assertThat(vote.getVotedResults().get(4).getUserIds().size()).isEqualTo(0);
        assertThat(vote.getVotedResults().get(4).getCnt()).isEqualTo(0);
    }

    @Test
    @DisplayName("유저 정보와 유저가 지목한 값으로 투표 결과를 갱신한다.")
    public void updateVoteResults() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("0", "1", "2", "3", "4");
        Vote vote = new Vote(gameId, userIds);

        //when

        updateVoteResultsByUsers(vote);


        //then
        assertThat(vote.getVotedResults().get(0).getCnt()).isEqualTo(0);
        assertThat(vote.getVotedResults().get(1).getCnt()).isEqualTo(2);
        assertThat(vote.getVotedResults().get(2).getCnt()).isEqualTo(2);
        assertThat(vote.getVotedResults().get(3).getCnt()).isEqualTo(1);
        assertThat(vote.getVotedResults().get(4).getCnt()).isEqualTo(0);

    }

    @Test
    @DisplayName("멀티 쓰레드 상황에서 유저 정보와 유저가 지목한 값으로 투표 결과를 갱신한다.")
    public void updateVoteResults_thread() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("0", "1", "2", "3", "4");
        Vote vote = new Vote(gameId, userIds);

        //when
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            String userId = String.valueOf(i);
            String liarId = String.valueOf(i % 3 + 1);
            threads[i] = new Thread(() -> vote.updateVoteResults(userId, liarId));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        //then
        assertThat(vote.getVotedResults().get(0).getCnt()).isEqualTo(0);
        assertThat(vote.getVotedResults().get(1).getCnt()).isEqualTo(2);
        assertThat(vote.getVotedResults().get(2).getCnt()).isEqualTo(2);
        assertThat(vote.getVotedResults().get(3).getCnt()).isEqualTo(1);
        assertThat(vote.getVotedResults().get(4).getCnt()).isEqualTo(0);
    }

    @Test
    @DisplayName("최다 득표 liar 후보 가져오기")
    public void getMaxVotedResult() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("0", "1", "2", "3", "4");
        Vote vote = new Vote(gameId, userIds);
        updateVoteResultsByUsers(vote);

        //when
        List<VotedResult> maxVotedResult = vote.getMostVotedResult();

        //then
        assertThat(maxVotedResult.size()).isEqualTo(2);
        assertThat(maxVotedResult.get(0).getCnt()).isEqualTo(2);
        assertThat(maxVotedResult.get(1).getCnt()).isEqualTo(2);
        assertThat(maxVotedResult.get(0).getLiarId()).isIn(Arrays.asList("1", "2"));
        assertThat(maxVotedResult.get(1).getLiarId()).isIn(Arrays.asList("1", "2"));
        assertThat(maxVotedResult.get(1).getUserIds().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("멀티 스레드 상황 최다 득표 liar 후보 가져오기")
    public void getMaxVotedResult_thread() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList("0", "1", "2", "3", "4");
        Vote vote = new Vote(gameId, userIds);

        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            String userId = String.valueOf(i);
            String liarId = String.valueOf(i % 3 + 1);
            threads[i] = new Thread(() -> vote.updateVoteResults(userId, liarId));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        //when
        List<VotedResult> maxVotedResult = vote.getMostVotedResult();

        //then
        assertThat(maxVotedResult.size()).isEqualTo(2);
        assertThat(maxVotedResult.get(0).getCnt()).isEqualTo(2);
        assertThat(maxVotedResult.get(1).getCnt()).isEqualTo(2);
        assertThat(maxVotedResult.get(0).getLiarId()).isIn(Arrays.asList("1", "2"));
        assertThat(maxVotedResult.get(1).getLiarId()).isIn(Arrays.asList("1", "2"));
        assertThat(maxVotedResult.get(1).getUserIds().size()).isEqualTo(2);
    }

    /**
     * 최다 득표는 userId 1, 2
     */
    private static void updateVoteResultsByUsers(Vote vote) {
        for (int i = 0; i < 5; i++) {
            String userId = String.valueOf(i);
            String liarId = String.valueOf(i % 3 + 1);
            vote.updateVoteResults(userId, liarId);
        }
    }

}