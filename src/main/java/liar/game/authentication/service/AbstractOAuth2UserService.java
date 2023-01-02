package liar.game.authentication.service;


import liar.game.authentication.domain.users.ProviderUser;
import liar.game.authentication.domain.users.social.GoogleUser;
import liar.game.authentication.domain.users.social.KakaoOidcUser;
import liar.game.authentication.domain.users.social.KakaoUser;
import liar.game.authentication.domain.users.social.NaverUser;
import liar.game.authentication.util.OAuth2Utils;
import liar.game.member.service.MemberService;
import liar.game.member.domain.Member;
import liar.game.member.repository.MemberRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Getter
public abstract class AbstractOAuth2UserService {

    private final MemberService memberService;

    private final MemberRepository memberRepository;


    @Autowired
    public AbstractOAuth2UserService(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest){

        Member member = memberRepository.findByEmail(providerUser.getEmail());

        if(member == null){
            ClientRegistration clientRegistration = userRequest.getClientRegistration();
            memberService.register(clientRegistration.getRegistrationId(),providerUser);
        }
    }

    public ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {

        String registrationId = clientRegistration.getRegistrationId();


        switch (registrationId) {

            case "google":
                return new GoogleUser(OAuth2Utils.getMainAttributes(oAuth2User), oAuth2User, clientRegistration);

            case "naver":
                return new NaverUser(OAuth2Utils.getSubAttributes(oAuth2User, "response"), oAuth2User, clientRegistration);

            case "kakao":
                if (oAuth2User instanceof OidcUser) {
                    return new KakaoOidcUser(OAuth2Utils.getMainAttributes(oAuth2User), oAuth2User, clientRegistration);

                } else {
                    return new KakaoUser(OAuth2Utils.getOtherAttributes(oAuth2User, "kakao_account", "profile"), oAuth2User, clientRegistration);
                }

            default:
                return null;
        }
    }
}
