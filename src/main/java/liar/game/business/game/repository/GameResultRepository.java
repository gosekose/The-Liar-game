package liar.game.business.game.repository;

import liar.game.business.game.domain.GameResult;
import liar.game.business.game.repository.dto.AllMemberGameResultAnalysisInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {


    // to do


//    @Query(value =
//            "select g.join_member_id as id, " +
//                    "sum(case when (g.result = 'WIN') then 1 else 0 end) as win, " +
//                    "sum(case when g.result = 'LOSE' then 1 else 0 end) as lose, " +
//                    "round(sum(case when (g.result = 'WIN') then 1 else 0 end) / (count(g.result) * 1.0) * 100, 1) as winning_rate, " +
//                    "max(g.modified_at) as last_join_game_time " +
//                    "from GameResult g " +
//                    "join Member m on g.join_member_id = m.member_id " +
//                    "group by g.join_member_id",
//            nativeQuery = true)
//    List<AllMemberGameResultAnalysisInterface> fetchAllMembersGameResultAnalysis();

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
