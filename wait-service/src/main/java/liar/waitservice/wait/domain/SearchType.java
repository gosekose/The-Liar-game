package liar.waitservice.wait.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchType {
    WAITROOMID("WAITOROOMID"),
    WAITROOMNAME("WAITROOMNAME"),
    HOSTNAME("HOSTNAME");

    private final String typeName;
}
