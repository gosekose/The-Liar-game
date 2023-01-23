package liar.game.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id @GeneratedValue
    @Column(name = "authority_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(STRING)
    private Authorities authorities;

    @Builder
    public Authority(Member member, Authorities authorities) {
        this.member = member;
        this.authorities = authorities;
    }

    public void updateUser(Member member) {
        this.member = member;
    }

}
