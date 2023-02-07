package liar.gameservice.other.member;

import liar.gameservice.exception.exception.NotFoundUserException;
import liar.gameservice.other.member.dao.MemberIdOnly;
import liar.gameservice.other.member.dao.MemberNameOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Flux<MemberIdOnly> findByUsername(String username) {
        return  Flux.fromIterable(memberRepository.findProjectionByUsername(username));
    }

    public Mono<MemberNameOnly> findUsernameById(String userId) {
        return Mono.fromCallable(() -> memberRepository.findProjectionByUserId(userId))
                .switchIfEmpty(Mono.error(new NotFoundUserException()));
    }

}
