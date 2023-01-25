package liar.memberservice.authentication.converter;

import liar.memberservice.authentication.domain.form.FormUser;
import liar.memberservice.authentication.domain.social.ProviderUser;
import liar.memberservice.member.domain.Member;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        Member member = providerUserRequest.getMember();
        if (member == null) {
            return null;
        }

        return FormUser.builder()
                .id(member.getEmail())
                .username(member.getUsername())
                .password(member.getPassword())
                .email(member.getEmail())
                .provider("none")
                .build();
    }
}
