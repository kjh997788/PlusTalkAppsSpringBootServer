package com.ChK.PlusTalk.controller;

import com.ChK.PlusTalk.constant.Gender;
import com.ChK.PlusTalk.dto.MemberRequestDto;
import com.ChK.PlusTalk.dto.MemberResponseDto;
import com.ChK.PlusTalk.entity.Member;
import com.ChK.PlusTalk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Member> registerMember(@RequestBody MemberRequestDto memberRequestDto) throws Exception {
        Member member = memberService.registerMember(memberRequestDto);
        return ResponseEntity.ok(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Member> loginMember(@RequestBody MemberRequestDto memberRequestDto) throws Exception {
        Member member = memberService.loginMember(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        return ResponseEntity.ok(member);
    }

    @GetMapping("/memberlist")
    public ResponseEntity<Member> memberList(@RequestParam String email) {
        Member members = memberService.memberList(email);
        return ResponseEntity.ok(members);
    }
}
