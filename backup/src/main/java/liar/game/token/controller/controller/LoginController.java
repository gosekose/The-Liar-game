package liar.game.token.controller.controller;

import jakarta.validation.Valid;
import liar.game.common.exception.exception.BindingInvalidException;
import liar.game.common.exception.exception.NotFoundUserException;
import liar.game.common.exception.exception.UserRegisterConflictException;
import liar.game.member.domain.Authority;
import liar.game.member.domain.Member;
import liar.game.member.service.AuthorityService;
import liar.game.member.service.MemberService;
import liar.game.member.service.dto.FormRegisterUserDto;
import liar.game.token.service.AuthService;
import liar.game.token.controller.dto.LoginDto;
import liar.game.token.controller.dto.TokenAuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final AuthorityService authorityService;

    private final AuthService authService;

    /**
     *
     * 회원 가입 요청
     * BAD_REQUEST: 400 (요청한 파라미터의 타입 에러 혹은 바인딩 에러)
     * CONFLICT: 409 (요청한 회원가입 이메일 이미 존재)
     * OK: 200 (가입 완료)
     *
     * @param formRegisterUserDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity formRegister(@Valid @RequestBody FormRegisterUserDto formRegisterUserDto,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BindingInvalidException();
        }

        if (!memberService.registerForm(formRegisterUserDto, passwordEncoder)) {
            throw new UserRegisterConflictException();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity logout(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("RefreshToken") String refreshToken) {

        authService.logout(accessToken, refreshToken);

        return new ResponseEntity(HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BindingInvalidException();
        }

        Member member = memberService.userLoginCheck(loginDto, passwordEncoder);
        if (member == null) {
            throw new NotFoundUserException();
        }

        List<Authority> authorities = authorityService.findAuthorityByUser(member);
        if (authorities.isEmpty()) {
            throw new NotFoundUserException();
        }

        return new ResponseEntity(authService.createFormTokenAuth(member.getEmail(), authorities), HttpStatus.OK);
    }


    @PostMapping("/reissue")
    public ResponseEntity reissueToken(@RequestHeader(value = "RefreshToken") String refreshToken) {

        TokenAuthDto tokenAuthDto = authService.reissue(refreshToken);

        return new ResponseEntity(tokenAuthDto, HttpStatus.OK);

    }


}
