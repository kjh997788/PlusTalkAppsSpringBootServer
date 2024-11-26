package com.ChK.PlusTalk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity // 디비로 생성
@Table(name = "friend") // 테이블 이름 정의
@Getter
@Setter
@ToString
public class Friend {
    @Id
    @Column(name = "friend_num")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long friendNum;
    private String memberEmail;
    private String friendMemberEmail;
    private String profileImageURL;
    private String name;
    private LocalDateTime friendSetTime;
}
