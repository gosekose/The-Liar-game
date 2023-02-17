package liar.gamemvcservice.game.service.vote;

import liar.gamemvcservice.game.domain.VotedResult;

import java.util.List;

public interface VotePolicy {

    void voteLiarUser();

    List<VotedResult> getMaxVotedLiarUser();



}
