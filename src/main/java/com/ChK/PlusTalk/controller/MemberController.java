package com.ChK.PlusTalk.controller;

import com.ChK.PlusTalk.constant.Gender;
import com.ChK.PlusTalk.dto.MemberRequestDto;
import com.ChK.PlusTalk.dto.MemberResponseDto;
import com.ChK.PlusTalk.entity.Member;
import com.ChK.PlusTalk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> registerMember(@RequestBody MemberRequestDto memberRequestDto) throws Exception {
        MemberResponseDto memberResponseDto = memberService.registerMember(memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Member> loginMember(@RequestBody MemberRequestDto memberRequestDto) throws Exception {
        Member member = memberService.loginMember(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        return ResponseEntity.ok(member);
    }

    @GetMapping("/memberinfo")
    public ResponseEntity<Member> memberList(@RequestParam String email) {
        Member member = memberService.memberList(email);
        return ResponseEntity.ok(member);
    }

}
