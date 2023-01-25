package liar.memberservice.authentication.service;

import liar.memberservice.authentication.converter.ProviderUserConverter;
import liar.memberservice.authentication.converter.ProviderUserRequest;
import liar.memberservice.authentication.domain.PrincipalUser;
import liar.memberservice.authentication.domain.social.ProviderUser;
import liar.memberservice.member.domain.Member;
import liar.memberservice.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    public CustomUserDetailsService(MemberService memberService, ProviderUserConverter<ProviderUserRequest,
                ProviderUser> providerUserConverter) {
        super(memberService, providerUserConverter);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = getMemberService().findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }


        // converter 처리
        ProviderUserRequest providerUserRequest = new ProviderUserRequest(member);
        ProviderUser providerUser = providerUser(providerUserRequest);

        return new PrincipalUser(providerUser);

    }



}

