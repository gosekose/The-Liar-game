package liar.waitservice.wait.domain;

import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class BaseEntity {

    @Id
    private String id;
}
