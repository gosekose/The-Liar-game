package liar.resultservice.result.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Level {
    BRONZE1(100L, 1),
    BRONZE2(300L, 2),
    SILVER1(500L, 3),
    SILVER2(800L,4),
    GOLD1(1200L, 5),
    GOLD2(1800L, 6),
    PLATINUM(2500L, 7),
    MASTER(3100L, 8),
    LIAR(5000L, 9);
    private final Long maxExp;
    private final Integer step;
}
