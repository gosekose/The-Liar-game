package liar.game.token.controller;

import liar.game.authentication.domain.PrincipalUser;
import liar.game.token.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginController {

    private final AuthService authService;

    /**
     *
     * 로그인 요청 콜백
     * 상태코드
     * NOT_FOUND: 400 (principalUser가 없을 경우, 혹은 인증이 안된 경우, UNAUTHORIZED 403이지만 404로 통일)
     * OK: 200 (로그인 완료, token, refreshToken 발급)
     *
     */
    @GetMapping("/")
    public ResponseEntity loginForProvideToken(@AuthenticationPrincipal PrincipalUser principalUser,
                                               Authentication authentication) {

        if (principalUser == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(authService.createOauthTokenAuth(authentication), HttpStatus.OK);
    }

}
