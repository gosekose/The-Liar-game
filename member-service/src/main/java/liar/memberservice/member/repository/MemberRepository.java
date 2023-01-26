package liar.memberservice.member.repository;

import liar.memberservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    Optional<Member> findByRegisterId(String registerId);
    Optional<Member> findByUserId(String userId);


}
