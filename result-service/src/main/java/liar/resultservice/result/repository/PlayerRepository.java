package liar.resultservice.result.repository;

import jakarta.persistence.LockModeType;
import liar.resultservice.other.member.Member;
import liar.resultservice.result.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    Player findPlayerByMember(Member member);
}
