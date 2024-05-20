package com.ChK.PlusTalk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity // 디비로 생성
@Table(name = "CHATROOM") // 테이블 이름 정의
@Getter
@Setter
@ToString
public class Chatroom {
    @Id
    @Column(name = "ROOM_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;
    private String roomName;
    private LocalDateTime createDate;
}
