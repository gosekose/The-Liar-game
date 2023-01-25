package liar.memberservice.authentication.converter;


import liar.memberservice.authentication.common.enums.SocialType;
import liar.memberservice.authentication.common.util.OAuth2Utils;
import liar.memberservice.authentication.domain.social.NaverUser;
import liar.memberservice.authentication.domain.social.ProviderUser;

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
