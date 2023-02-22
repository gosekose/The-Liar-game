package liar.resultservice.common.authentication;

import jakarta.persistence.*;
import liar.resultservice.other.member.Member;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id
    @GeneratedValue
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
}
