package liar.waitservice.wait.domain.utils;

import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class BaseRedisTemplateEntity<T> {

    @Id
    private T id;
}
