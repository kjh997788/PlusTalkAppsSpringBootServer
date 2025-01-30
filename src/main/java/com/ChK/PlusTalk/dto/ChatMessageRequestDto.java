package com.ChK.PlusTalk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequestDto {
    private String memberEmail;
    private String chatRoomId;
    private String senderEmail;
    private int messageId;
    private String messageText;
    private Boolean isImage;
}
