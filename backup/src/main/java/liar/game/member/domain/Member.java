package liar.game.member.domain;

import jakarta.persistence.*;
import liar.game.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String registrationId;

    private String registerId;

    private String password;
    private String email;
    private String picture;

    @OneToMany(mappedBy = "member")
    private List<Authority> authorities = new ArrayList<>();

    private String username;


    @Builder
    public Member(String password, String registrationId, String registerId,
                 String email, String picture, String username) {
        this.username = username;
        this.password = password;
        this.registrationId = registrationId;
        this.registerId = registerId;
        this.email = email;
        this.picture = picture;
    }

    public void addAuthorities(Authority authority) {
        this.authorities.add(authority);
        authority.updateUser(this);
    }
}
