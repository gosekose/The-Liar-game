package liar.gameservice.game.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
public class BaseRedisTemplateEntity<T> {

    @Id
    private T id;

    public BaseRedisTemplateEntity() {
    }

    public BaseRedisTemplateEntity(T id) {
        this.id = id;
    }
}
