package liar.resultservice.other.member;

import liar.resultservice.other.member.dao.UserIdOnly;
import liar.resultservice.other.member.dao.UserNameOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    UserNameOnly findProjectionByUserId(String userId);

    List<UserIdOnly> findProjectionByUsername(String userName);

    UserIdOnly findProjectionById(AtomicLong id);
}
