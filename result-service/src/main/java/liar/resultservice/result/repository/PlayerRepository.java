package liar.resultservice.result.repository;

import jakarta.persistence.LockModeType;
import liar.resultservice.other.member.Member;
import liar.resultservice.result.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Player p where p.member = :member")
    Player findWithMemberForUpdate(@Param("member") Member member);

    Player findPlayerByMember(Member member);

    @Query(value = "SELECT * FROM player WHERE member_id = :memberId FOR UPDATE", nativeQuery = true)
    Player findWithMemberForUpdate(@Param("memberId") Long memberId);
}
