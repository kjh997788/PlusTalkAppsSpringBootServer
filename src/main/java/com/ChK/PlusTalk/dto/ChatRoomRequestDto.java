package com.ChK.PlusTalk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequestDto {
    private String memberEmail;
    private String friendEmail;
    private String chatRoomId;
}