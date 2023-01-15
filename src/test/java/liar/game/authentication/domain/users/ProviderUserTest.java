package liar.game.authentication.domain.users;

import liar.game.authentication.domain.Attributes;
import liar.game.authentication.domain.social.GoogleUser;
import liar.game.authentication.domain.social.ProviderUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ExtendWith(MockitoExtension.class)
class ProviderUserTest {

    @Mock
    OAuth2User oAuth2User;

    @Mock
    Attributes attributes;

    @Mock
    ClientRegistration clientRegistration;

    @Test
    public void 인터페이스_테스트() throws Exception {
        //given
        ProviderUser providerUser = new GoogleUser(attributes, oAuth2User, clientRegistration);

        //when

        //then

    }

}
