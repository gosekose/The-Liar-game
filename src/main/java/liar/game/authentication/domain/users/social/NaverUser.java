package liar.game.authentication.domain.users.social;

import liar.game.authentication.domain.Attributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Slf4j
public class NaverUser extends OAuth2ProviderUser {

    public NaverUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration){
        super(attributes.getSubAttributes(), oAuth2User, clientRegistration);
    }


    @Override
    public String getId() {
        return (String)getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String)getAttributes().get("email");
    }

    @Override
    public String getPicture() {
        return (String)getAttributes().get("profile_image");
    }
}