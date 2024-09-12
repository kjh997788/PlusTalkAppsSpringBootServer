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

    private String memberEmail;
    @Id
    @Column(name = "FRIEND_NUM")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long friendNum;
    private String friendMemberEmail;
    private LocalDateTime friendSetTime;
}
