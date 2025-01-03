package com.ChK.PlusTalk.entity;


import com.ChK.PlusTalk.constant.Authority;
import com.ChK.PlusTalk.constant.Existence;
import com.ChK.PlusTalk.constant.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity // 디비로 생성
@Table(name = "member") // 테이블 이름 정의
@Getter
@Setter
@ToString
@NoArgsConstructor // 디폴트 생성자 생성
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
//    @Column(unique = true)
    private String phone;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "sign_up_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime signUpTime;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    private String intro;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Enumerated(EnumType.STRING)
    private Existence existence;

    @Builder
    public Member(String email, String password, String name, String phone, LocalDate birthday, Gender gender, LocalDateTime signUpTime, Authority authority, Existence existence) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
        this.signUpTime = signUpTime != null ? signUpTime.withNano(0) : null;
        this.authority = authority;
    }

    // 타 테이블 맵핑


}
