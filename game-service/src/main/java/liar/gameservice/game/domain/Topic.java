package liar.gameservice.game.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash(value = "Topic")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic extends BaseRedisTemplateEntity<Long> {

    @Id
    private Long id;
    private String name;

    public Topic(Long id, String name) {
        super(id);
        this.name = name;
    }


}
