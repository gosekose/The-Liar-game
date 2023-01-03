package liar.game.authentication.service;

import liar.game.authentication.domain.users.ProviderUser;
import liar.game.authentication.domain.users.social.GoogleUser;
import liar.game.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CustomOAuth2UserServiceTest {

    @Mock
    ClientRegistration clientRegistration;

    @Mock
    OAuth2User oAuth2User;

    @Mock
    MemberService memberService;

    @Mock
    ProviderUserService providerUserService;

    @Test
    @DisplayName("oauth2 provider를 체크하기")
    public void providerUser() throws Exception {

        //given
        when(clientRegistration.getRegistrationId()).thenReturn("google");

        //when
        CustomOAuth2UserService oAuth2UserService = new CustomOAuth2UserService(memberService);
        ProviderUser providerUser = oAuth2UserService.providerUser(clientRegistration, oAuth2User);

        //then
        assertThat(providerUser).isInstanceOf(GoogleUser.class);
    }

    @Test
    @DisplayName("oauth2 회원가입 등록하기")
    public void register() throws Exception {
        //given
        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        when(clientRegistration.getRegistrationId()).thenReturn("google");

        CustomOAuth2UserService oAuth2UserService = mock(CustomOAuth2UserService.class);
        ProviderUser providerUser = oAuth2UserService.providerUser(clientRegistration, oAuth2User);

        //when
        when(providerUserService.getProviderUser(userRequest)).thenReturn(providerUser);
        CustomOAuth2UserServiceMock customOAuth2UserServiceMock = new CustomOAuth2UserServiceMock(memberService, providerUserService);
        OAuth2User oAuth2User = customOAuth2UserServiceMock.loadUser(userRequest);

        //then
        assertThat(oAuth2User).isNotNull();
    }


}