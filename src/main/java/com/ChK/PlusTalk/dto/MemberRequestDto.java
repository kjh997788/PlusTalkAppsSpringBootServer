package com.ChK.PlusTalk.dto;

import com.ChK.PlusTalk.constant.Authority;
import com.ChK.PlusTalk.constant.Gender;
import com.ChK.PlusTalk.constant.Existence;
import com.ChK.PlusTalk.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON에서 제외
public class MemberRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private LocalDate birthday;
    private String intro;
    private String profileImageUrl;
    private Gender gender;
    private Authority authority;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // 암호화된 값으로 DB에 저장됨
                .name(name)
                .phone(phone)
                .birthday(birthday)
                .gender(gender)
                .signUpTime(LocalDateTime.now()) // 오늘 날짜 입력(2023-01-01 같은 형식)
                .existence(Existence.YES) // 자동으로 존재 회원
                .authority(authority) // 회원 종류
                .build();
    }

    // 로그인시 넘겨받은 아이디와 비밀번호 조합으로 토큰 생성 준비
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
