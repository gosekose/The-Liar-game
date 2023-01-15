package liar.game.authentication.converter;

import liar.game.authentication.domain.social.GoogleUser;
import liar.game.authentication.domain.social.ProviderUser;
import liar.game.authentication.common.enums.SocialType;
import liar.game.authentication.common.util.OAuth2Utils;

public final class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.getClientRegistration().getRegistrationId().equals(
                SocialType.GOOGLE.getSocialName()
        )) return null;

        return new GoogleUser(
                OAuth2Utils.getMainAttributes(providerUserRequest.getOAuth2User()),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration()
        );
    }
}
