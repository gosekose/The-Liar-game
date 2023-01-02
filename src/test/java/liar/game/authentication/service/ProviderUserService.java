package liar.game.authentication.service;

import liar.game.authentication.domain.users.ProviderUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

interface ProviderUserService {
    ProviderUser getProviderUser(OAuth2UserRequest userRequest);
}
