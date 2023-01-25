package liar.game.business.game.repository;

import liar.game.business.game.domain.GameResult;
import liar.game.business.game.repository.dto.AllMemberGameResultAnalysisInterface;
import liar.game.business.game.repository.dto.AllMemberGameResultAnalysisJpqlInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {


    @Query(value =
            "select g.member.id as id, " +
                    "sum(case when (g.result = 'WIN') then 1 else 0 end) as win, " +
                    "sum(case when g.result = 'LOSE' then 1 else 0 end) as lose, " +
                    "round(sum(case when (g.result = 'WIN') then 1 else 0 end) / (count(g.result) * 1.0) * 100, 1) as rate, " +
                    "max(g.modifiedAt) as lastJoinGameTime " +
                    "from GameResult g " +
                    "join Member m on g.member.id = m.id " +
                    "group by g.member.id")
    Page<AllMemberGameResultAnalysisJpqlInterface> fetchAllMembersGameResultAnalysisJpql(Pageable pageable);

    @Query(value =
            "select g.join_member_id as id, " +
                    "sum(case when (g.result = 'WIN') then 1 else 0 end) as win, " +
                    "sum(case when g.result = 'LOSE' then 1 else 0 end) as lose, " +
                    "round(sum(case when (g.result = 'WIN') then 1 else 0 end) / (count(g.result) * 1.0) * 100, 1) as rate, " +
                    "max(g.modified_at) as lastjointime " +
                    "from GameResult g " +
                    "join Member m on g.join_member_id = m.member_id " +
                    "group by g.join_member_id " +
                    "order by g.join_member_id ASC",
            countQuery = "select count(*) from GameResult g",
            nativeQuery = true)
    Page<AllMemberGameResultAnalysisInterface> fetchAllMembersGameResultAnalysis(Pageable pageable);

}
