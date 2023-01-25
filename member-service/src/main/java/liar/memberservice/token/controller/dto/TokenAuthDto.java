package liar.memberservice.token.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenAuthDto {

    String accessToken;

    String refreshToken;

    @Builder
    public TokenAuthDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenAuthDto of (String accessToken, String refreshToken) {
        return new TokenAuthDto(accessToken, refreshToken);
    }
}
