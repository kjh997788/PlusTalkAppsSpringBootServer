package com.ChK.PlusTalk.service;

import com.ChK.PlusTalk.constant.Authority;
import com.ChK.PlusTalk.constant.Existence;
import com.ChK.PlusTalk.constant.Gender;
import com.ChK.PlusTalk.dto.MemberRequestDto;
import com.ChK.PlusTalk.dto.MemberResponseDto;
import com.ChK.PlusTalk.entity.Member;
import com.ChK.PlusTalk.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public MemberResponseDto registerMember(MemberRequestDto memberRequestDto) {
        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(memberRequestDto.getPassword());

        // 회원 엔티티 생성
        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .password(encodedPassword)
//                .name(memberRequestDto.getName())
//                .phone(memberRequestDto.getPhone())
//                .birthday(memberRequestDto.getBirthday())
//                .gender(memberRequestDto.getGender())
                .signUpTime(LocalDateTime.now())
                .authority(Authority.ROLE_MEMBER) // 기본 권한 설정
                .existence(Existence.YES) // 기본 존재 상태 설정
                .build();

        // 회원 저장
        Member savedMember = memberRepository.save(member);

        // MemberResponseDto로 변환 후 반환
        return MemberResponseDto.builder()
//                .memberId(savedMember.getMemberId())
                .email(savedMember.getEmail())
                .signUpTime(savedMember.getSignUpTime())
                .authority(savedMember.getAuthority())
                .existence(savedMember.getExistence())
                .build();
    }

    public MemberResponseDto loginMember(String email, String password) throws Exception {
        // 이메일로 회원 조회
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            // 비밀번호 검증
            if (passwordEncoder.matches(password, member.getPassword())) {
                // 세션에 회원 정보 저장
                session.setAttribute("member", member);
                return MemberResponseDto.builder()
//                        .memberId(member.getMemberId())
                        .email(member.getEmail())
                        .authority(member.getAuthority())
                        .existence(member.getExistence())
                        .build();
            } else {
                throw new Exception("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new Exception("회원 정보를 찾을 수 없습니다.");
        }
    }

    // 멤버 정보 조회
    public MemberResponseDto memberList(String email) throws Exception {
        Optional<Member> optionalMember =memberRepository.findByEmail(email);
        if (optionalMember.isPresent()){
            Member member = optionalMember.get();
                return MemberResponseDto.builder()
//                        .memberId(member.getMemberId())
                        .email(member.getEmail())
                        .name(member.getName())
                        .phone(member.getPhone())
                        .birthday(member.getBirthday())
                        .gender(member.getGender())
                        .signUpTime(member.getSignUpTime())
                        .profileImageUrl(s3Service.generateGetPreSignedUrl(member.getEmail()))
                        .intro(member.getIntro())
                        .authority(member.getAuthority())
                        .existence(member.getExistence())
                        .build();
        } else {
            throw new Exception("회원 정보를 찾을 수 없습니다.");
        }
    }

    // 멤버 정보 수정
    public MemberResponseDto updateMember(MemberRequestDto memberRequestDto) {
        // 이메일로 회원 조회
        Optional<Member> optionalMember = memberRepository.findByEmail(memberRequestDto.getEmail());
        if (optionalMember.isEmpty()) {
            throw new RuntimeException("해당 이메일로 가입된 회원을 찾을 수 없습니다.");
        }

        Member member = optionalMember.get();

        // 회원 정보 업데이트 (null이 아닌 필드만 업데이트)
        if (memberRequestDto.getName() != null) {
            member.setName(memberRequestDto.getName());
        }
        if (memberRequestDto.getPhone() != null) {
            member.setPhone(memberRequestDto.getPhone());
        }
        if (memberRequestDto.getBirthday() != null) {
            member.setBirthday(memberRequestDto.getBirthday());
        }
        if (memberRequestDto.getGender() != null) {
            member.setGender(memberRequestDto.getGender());
        }
        if (memberRequestDto.getIntro() != null) {
            member.setIntro(memberRequestDto.getIntro());
        }
        if (memberRequestDto.getProfileImageUrl() != null) {
            member.setProfileImageUrl(memberRequestDto.getProfileImageUrl());
        }

        // 수정된 회원 정보를 저장
        Member updatedMember = memberRepository.save(member);

        // 수정된 회원 정보를 MemberResponseDto로 변환하여 반환
        return MemberResponseDto.builder()
                .email(updatedMember.getEmail())
                .name(updatedMember.getName())
                .phone(updatedMember.getPhone())
                .birthday(updatedMember.getBirthday())
                .gender(updatedMember.getGender())
                .signUpTime(updatedMember.getSignUpTime())
                .profileImageUrl(updatedMember.getProfileImageUrl())
                .intro(updatedMember.getIntro())
                .authority(updatedMember.getAuthority())
                .existence(updatedMember.getExistence())
                .build();
    }

}
