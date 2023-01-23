package liar.game.authentication.domain.form;

import liar.game.authentication.domain.social.ProviderUser;
import liar.game.member.domain.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class FormUser implements ProviderUser {

    private String registrationId;
    private String id;
    private String username;
    private String password;
    private String email;
    private String provider;
    private List<? extends GrantedAuthority> authorities;

    private Member users;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public String getPicture() {
        return null;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public OAuth2User getOAuth2User() {
        return null;
    }

}
