package com.ChK.PlusTalk.dto;

import com.ChK.PlusTalk.constant.Authority;
import com.ChK.PlusTalk.constant.Existence;
import com.ChK.PlusTalk.constant.Gender;
import com.ChK.PlusTalk.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON에서 제외
public class MemberResponseDto {
    private Long memberId;
    private String email;
    private String name;
    private String phone;
    private LocalDate birthday;
    private Gender gender;
    private LocalDateTime signUpTime;
    private String profileImageUrl;
    private String intro;
    private Authority authority;
    private Existence existence;


    public static MemberResponseDto of(Member member){
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .build();
    }
}
