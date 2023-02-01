package liar.waitservice.wait.domain;

import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class BaseEntity<T> {

    @Id
    private T id;
}
