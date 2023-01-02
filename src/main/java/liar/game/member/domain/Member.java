package liar.game.member.domain;

import liar.game.authentication.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String registrationId;

    private String registerId;

    private String password;
    private String email;
    private String picture;

    private String authorities;

    @Builder
    public Member(String registrationId, String registerId, String password, String email, String picture, String authorities) {
        this.registrationId = registrationId;
        this.registerId = registerId;
        this.password = password;
        this.email = email;
        this.picture = picture;
        this.authorities = authorities;
    }
}
