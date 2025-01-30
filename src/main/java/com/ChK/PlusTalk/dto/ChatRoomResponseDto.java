package com.ChK.PlusTalk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private String memberEmail;
    private String friendEmail;
    private String chatRoomId;
    private String createdTime;
}
