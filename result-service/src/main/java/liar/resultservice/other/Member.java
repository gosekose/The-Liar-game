package liar.resultservice.other;

import jakarta.persistence.*;
import liar.resultservice.result.domain.Player;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements Serializable {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private AtomicLong id;
    private String userId;
    private String username;

    @OneToOne(
            fetch = FetchType.LAZY,
            mappedBy = "member",
            cascade = CascadeType.ALL)
    private Player player;
}
