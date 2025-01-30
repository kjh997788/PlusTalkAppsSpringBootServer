package com.ChK.PlusTalk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessageResponseDto {
    private String chatRoomId;
    private int messageId;
    private String senderEmail;
    private String messageTime;
    private String messageText;
    private String imageUrl;

    @JsonProperty("isImage")  // ✅ JSON에서 필드명을 `isImage`로 강제 지정
    private boolean isImage;
}
