package liar.waitservice.other;

import liar.waitservice.other.dao.MemberIdOnly;
import liar.waitservice.other.dao.MemberNameOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberIdOnly> findByUsername(String username) {
        return memberRepository.findProjectionByUsername(username);
    }

    public MemberNameOnly findUsernameById(String userId) {
        return memberRepository.findProjectionByUserId(userId);
    }

}
