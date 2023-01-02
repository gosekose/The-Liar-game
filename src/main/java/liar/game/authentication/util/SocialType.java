package liar.game.authentication.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType{
    GOOGLE("google"),
    APPLE("apple"),
    FACEBOOK("facebook"),
    NAVER("naver"),
    KAKAO("kakao");
    private final String socialName;
}