package liar.game.authentication.converter;

import liar.game.member.domain.Member;
import lombok.Getter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class ProviderUserRequest {

    private final ClientRegistration clientRegistration;
    private final OAuth2User oAuth2User;
    private final Member member;

    public ProviderUserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        this.clientRegistration = clientRegistration;
        this.oAuth2User = oAuth2User;
        this.member = null;
    }

    public ProviderUserRequest(Member member) {
        this.clientRegistration = null;
        this.oAuth2User = null;
        this.member = member;
    }
}


