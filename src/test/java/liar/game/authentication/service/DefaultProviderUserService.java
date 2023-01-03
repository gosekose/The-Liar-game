package liar.game.authentication.service;

import liar.game.authentication.domain.users.ProviderUser;
import liar.game.member.service.MemberService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

class DefaultProviderUserService extends AbstractOAuth2UserService implements ProviderUserService {

    public DefaultProviderUserService(MemberService memberService) {
        super(memberService);
    }

    @Override
    public ProviderUser getProviderUser(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        return providerUser(clientRegistration, oAuth2User);
    }

}