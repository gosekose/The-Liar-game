package liar.memberservice.member.service;

import liar.memberservice.authentication.domain.social.ProviderUser;
import liar.memberservice.exception.exception.NotFoundUserException;
import liar.memberservice.exception.exception.UserRegisterConflictException;
import liar.memberservice.member.domain.Authority;
import liar.memberservice.member.domain.Member;
import liar.memberservice.member.repository.AuthorityRepository;
import liar.memberservice.member.repository.MemberRepository;
import liar.memberservice.member.service.dto.FormRegisterUserDto;
import liar.memberservice.token.controller.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static liar.memberservice.member.domain.Authorities.ROLE_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;


    public Member find(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> {throw new NotFoundUserException();});
    }

    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId).orElseThrow(() -> {throw new NotFoundUserException();});
    }

    public Member findByEmailNoOptional(String email) {
        return memberRepository.findByEmail(email);
    }


    public Member findByRegisterId(String registeredId) {
        return memberRepository.findByRegisterId(registeredId).orElseThrow(() -> {throw new NotFoundUserException();});
    }

    @Transactional
    public Long save(Member member) {
        Member saveMember = memberRepository.save(member);
        return saveMember.getId();
    }

    public Long getUserId(String email) {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new NotFoundUserException();
        }

        return member.getId();
    }

    public Member findOne(Long id) {
        Optional<Member> optionalUser = memberRepository.findById(id);

        return optionalUser.orElseThrow(() -> {
            throw new NotFoundUserException();
        });
    }

    @Transactional
    public void register(String registrationId, ProviderUser providerUser) {
        Member savedMember = memberRepository.save(
                Member.builder()
                        .userId(UUID.randomUUID().toString())
                        .registrationId(registrationId)
                        .registerId(providerUser.getId())
                        .password(providerUser.getPassword())
                        .email(providerUser.getEmail())
                        .picture(providerUser.getPicture())
                        .build()
        );

        authorityRepository.save(
                Authority.builder().member(savedMember).authorities(ROLE_USER).build()
        );

    }

    @Transactional
    public boolean registerForm(FormRegisterUserDto formRegisterUserDto, PasswordEncoder passwordEncoder) {

        Member findMember = memberRepository.findByEmail(formRegisterUserDto.getEmail());

        if (findMember == null)  {

            Member user = Member.builder()
                    .userId(UUID.randomUUID().toString())
                    .username(formRegisterUserDto.getUsername())
                    .password(passwordEncoder.encode(formRegisterUserDto.getPassword()))
                    .email(formRegisterUserDto.getEmail())
                    .build();

            memberRepository.save(user);

            authorityRepository.save(Authority.builder().member(user).authorities(ROLE_USER).build());

            return true;
        }
        else {
            throw new UserRegisterConflictException();
        }
    }

    public Member userLoginCheck(LoginDto loginDto, PasswordEncoder passwordEncoder) {
        return userCheck(loginDto, passwordEncoder);
    }


    /**
     * 이메일로 유저 찾기
     */
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    /**
     * 로그인 정보를 입력 받아 존재하는 회원인지 판단
     */
    private Member userCheck(LoginDto loginDto, PasswordEncoder passwordEncoder) {

        Member findUser = findByEmail(loginDto.getEmail());

        if (findUser == null || !passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword())) {
            return null;
        }

        return findUser;
    }


    public Member findUserForAuthentication(Authentication authentication) {
        return findByEmail(authentication.getName());
    }


}
