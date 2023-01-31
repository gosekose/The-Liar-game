package liar.waitservice.wait.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@RedisHash("onlineHost")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnlineHost implements Serializable {

    @Id
    private String id;
    private String waitRoomId;
}
