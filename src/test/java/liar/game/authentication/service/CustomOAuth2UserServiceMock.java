//package liar.game.authentication.service;
//
//
//import liar.game.authentication.application.service.AbstractOAuth2UserService;
//import liar.game.authentication.domain.PrincipalUser;
//import liar.game.authentication.domain.social.ProviderUser;
//import liar.game.member.service.MemberService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//public class CustomOAuth2UserServiceMock extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final ProviderUserService providerUserService;
//
//    public CustomOAuth2UserServiceMock(MemberService memberService,
//                                       ProviderUserService providerUserService) {
//        super(memberService);
//        this.providerUserService = providerUserService;
//    }
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//        ProviderUser providerUser = providerUserService.getProviderUser(userRequest);
//
//        return new PrincipalUser(providerUser);
//    }
//}