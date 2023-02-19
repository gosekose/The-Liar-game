package liar.resultservice.common.token.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthDto {

    String accessToken;

    String refreshToken;
    String userId;

    @Builder
    public AuthDto(String accessToken, String refreshToken, String userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public static AuthDto of (String accessToken, String refreshToken, String userId) {
        return new AuthDto(accessToken, refreshToken, userId);
    }
}
