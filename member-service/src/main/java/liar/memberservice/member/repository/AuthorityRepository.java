package liar.memberservice.member.repository;

import liar.memberservice.member.domain.Authority;
import liar.memberservice.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @EntityGraph(attributePaths = "member")
    List<Authority> findAuthorityByMember(Member member);
}
