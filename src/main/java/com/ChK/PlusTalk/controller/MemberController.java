package com.ChK.PlusTalk.controller;

import com.ChK.PlusTalk.constant.Gender;
import com.ChK.PlusTalk.dto.MemberRequestDto;
import com.ChK.PlusTalk.dto.MemberResponseDto;
import com.ChK.PlusTalk.entity.Member;
import com.ChK.PlusTalk.service.MemberService;
import com.ChK.PlusTalk.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final S3Service s3Service;

    // 회원 가입
    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> registerMember(@RequestBody MemberRequestDto memberRequestDto) throws Exception {
        MemberResponseDto memberResponseDto = memberService.registerMember(memberRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> loginMember(@RequestBody MemberRequestDto memberRequestDto) throws Exception {
        MemberResponseDto memberResponseDto = memberService.loginMember(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    // 특정 회원 정보 전체 조회 
    @PostMapping("/info")
    public ResponseEntity<MemberResponseDto> memberList(@RequestBody MemberRequestDto memberRequestDto) throws Exception {
        MemberResponseDto memberResponseDto = memberService.memberList(memberRequestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    // 특정 회원 정보 수정
    @PostMapping("/update")
    public ResponseEntity<MemberResponseDto> memberProfileUpdate(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.updateMember(memberRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    // 특정 회원 프로필 이미지 저장, 수정
    @PostMapping("/profile-image-upload-url")
    public ResponseEntity<String> memberProfileImageUpdate(@RequestBody MemberRequestDto memberRequestDto) {
        long memberId = memberService.findMemberIdByMemberEmail(memberRequestDto.getEmail());
        String preSignedUrl = s3Service.generatePutPreSignedUrlForMemberProfileImage(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(preSignedUrl);
    }

}
