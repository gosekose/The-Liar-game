package liar.game.authentication.converter;

import liar.game.authentication.domain.form.FormUser;
import liar.game.authentication.domain.social.ProviderUser;
import liar.game.member.domain.Member;
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
