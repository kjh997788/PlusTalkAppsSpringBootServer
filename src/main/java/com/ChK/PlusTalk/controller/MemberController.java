package com.ChK.PlusTalk.controller;

import com.ChK.PlusTalk.dto.MemberRequestDto;
import com.ChK.PlusTalk.dto.MemberResponseDto;
import com.ChK.PlusTalk.service.MemberService;
import com.ChK.PlusTalk.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final S3Service s3Service;

    // 회원 가입
    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@RequestBody MemberRequestDto memberRequestDto) {
        try {
            MemberResponseDto responseDto = memberService.registerMember(memberRequestDto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "회원 가입 성공",
                            "data", responseDto
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", 409,
                            "message", e.getMessage(),
                            "data", ""
                    ));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberRequestDto memberRequestDto) {
        try {
            MemberResponseDto memberResponseDto = memberService.loginMember(memberRequestDto.getEmail(), memberRequestDto.getPassword());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "로그인 성공",
                            "data", Map.of(
                                    "email", memberResponseDto.getEmail(),
                                    "authority", memberResponseDto.getAuthority()
                            )
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "message", e.getMessage(),
                            "data", ""
                    ));
        }
    }


    // 특정 회원 정보 전체 조회 
    @PostMapping("/info")
    public ResponseEntity<?> memberList(@RequestBody MemberRequestDto memberRequestDto) {
        try {
            MemberResponseDto memberResponseDto = memberService.memberList(memberRequestDto.getEmail());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "회원 정보 조회 성공",
                            "data", memberResponseDto
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "message", "일치하는 회원이 없음",
                            "data", ""
                    ));
        }
    }

    // 특정 회원 정보 수정
    @PostMapping("/update")
    public ResponseEntity<?> memberProfileUpdate(@RequestBody MemberRequestDto memberRequestDto) {
        try {
            MemberResponseDto memberResponseDto = memberService.updateMember(memberRequestDto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "회원 정보 수정 성공",
                            "data", memberResponseDto
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", 400,
                            "message", e.getMessage(),
                            "data", ""
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "회원 정보 수정 중 오류 발생",
                            "data", ""
                    ));
        }
    }

    // 특정 회원 프로필 이미지 저장, 수정
    @PostMapping("/profile-image-upload-url")
    public ResponseEntity<?> memberProfileImageUpdate(@RequestBody MemberRequestDto memberRequestDto) {
        try {
            long memberId = memberService.findMemberIdByMemberEmail(memberRequestDto.getEmail());

            if (memberId == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", 404,
                                "message", "해당 이메일로 가입된 회원을 찾을 수 없습니다.",
                                "data", ""
                        ));
            }

            String preSignedUrl = s3Service.generatePutPreSignedUrlForMemberProfileImage(memberId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "프로필 업로드용 pre-signed URL 생성 성공",
                            "data", Map.of(
                                    "profileThumbnailURL", preSignedUrl
                            )
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "프로필 업로드용 pre-signed URL 생성 중 오류 발생",
                            "data", ""
                    ));
        }
    }

}
