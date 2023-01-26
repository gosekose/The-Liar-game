package liar.memberservice.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import liar.memberservice.member.domain.Member;
import liar.memberservice.member.service.MemberService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member-service/users")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity getMemberInfo(@RequestHeader String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new MemberInfoDto(memberService.findByUserId(userId)));
    }

    @Data
    @NoArgsConstructor
    static class MemberInfoDto {
        private String email;
        private String username;

        public MemberInfoDto(Member member) {
            this.email = member.getEmail();
            this.username = member.getUsername();
        }
    }

}
