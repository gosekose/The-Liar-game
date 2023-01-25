package liar.memberservice.authentication.converter;

import liar.memberservice.authentication.common.enums.SocialType;
import liar.memberservice.authentication.common.util.OAuth2Utils;
import liar.memberservice.authentication.domain.social.GoogleUser;
import liar.memberservice.authentication.domain.social.ProviderUser;

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
