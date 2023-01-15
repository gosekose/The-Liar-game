package liar.game.authentication.common.enums;

public enum SocialType {

    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");
    private final String socialName;

    private SocialType(String socialName) {
        this.socialName = socialName;
    }

    public String getSocialName() {
        return socialName;
    }

}
