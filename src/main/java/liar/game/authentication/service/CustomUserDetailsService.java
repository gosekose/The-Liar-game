package liar.game.authentication.service;

import liar.game.member.service.MemberService;
import liar.game.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {
    public CustomUserDetailsService(MemberService memberService) {
        super(memberService);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return null;
    }
    
}

