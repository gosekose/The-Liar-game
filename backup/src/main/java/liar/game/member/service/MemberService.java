package liar.game.member.service;

import liar.game.authentication.domain.social.ProviderUser;
import liar.game.common.exception.exception.UserRegisterConflictException;
import liar.game.member.domain.Authority;
import liar.game.member.domain.Member;
import liar.game.member.repository.AuthorityRepository;
import liar.game.member.repository.MemberRepository;
import liar.game.member.service.dto.FormRegisterUserDto;
import liar.game.token.controller.dto.LoginDto;
import liar.game.common.exception.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static liar.game.member.domain.Authorities.ROLE_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;


    public Member find(Long id) {

        Optional<Member> findMember = memberRepository.findById(id);
        Member member = findMember.orElseThrow(() -> {throw new NotFoundUserException();});

        return member;
    }

    public Member findByEmailNoOptional(String email) {
        return memberRepository.findByEmail(email);
    }


    public Member findByRegisterId(String registeredId) {
        Optional<Member> findMember = memberRepository.findByRegisterId(registeredId);
        Member member = findMember.orElseThrow(
                () -> {
                    throw new NotFoundUserException();
                }
        );

        return member;
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
