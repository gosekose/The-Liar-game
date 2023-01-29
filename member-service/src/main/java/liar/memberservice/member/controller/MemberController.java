package liar.memberservice.member.controller;

import liar.memberservice.member.controller.dto.MemberInfoDto;
import liar.memberservice.member.service.MemberService;
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
}
