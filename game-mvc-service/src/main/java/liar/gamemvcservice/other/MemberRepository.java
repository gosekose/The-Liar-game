package liar.gamemvcservice.other;

import liar.gamemvcservice.other.dao.MemberIdOnly;
import liar.gamemvcservice.other.dao.MemberNameOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    MemberNameOnly findProjectionByUserId(String userId);

    List<MemberIdOnly> findProjectionByUsername(String userName);
}
