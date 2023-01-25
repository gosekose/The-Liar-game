package liar.memberservice.member.service;

import liar.memberservice.member.domain.Authority;
import liar.memberservice.member.domain.Member;
import liar.memberservice.member.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public List<Authority> findAuthorityByUser(Member member) {
        return authorityRepository.findAuthorityByMember(member);
    }


}
