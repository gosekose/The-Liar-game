package liar.game.authentication.converter;

import liar.game.authentication.domain.social.NaverUser;
import liar.game.authentication.domain.social.ProviderUser;
import liar.game.authentication.common.enums.SocialType;
import liar.game.authentication.common.util.OAuth2Utils;

public final class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.getClientRegistration().getRegistrationId().equals(
                SocialType.NAVER.getSocialName()
        )) return null;

        return new NaverUser(
                OAuth2Utils.getSubAttributes(providerUserRequest.getOAuth2User(), "response"),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration()
        );

    }
}
