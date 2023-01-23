package liar.game.business.channel.domain;

import liar.game.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Channel {

    @Id @GeneratedValue
    @Column(name = "channel_id")
    private Long id;

    private String channelName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @Enumerated(STRING)
    private GameSequence gameSequence;
    private int maxMember;

}