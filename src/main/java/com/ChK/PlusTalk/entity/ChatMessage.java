package com.ChK.PlusTalk.entity;

import lombok.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String chatRoomId;
    private int messageId;
    private String senderEmail;
    private String messageTime;
    private boolean isImage;
    private String messageText;

    // DynamoDB 저장을 위한 변환
    public Map<String, AttributeValue> toDynamoDBItem() {
        return Map.of(
                "chatRoomId", AttributeValue.builder().s(chatRoomId).build(),
                "messageId", AttributeValue.builder().n(String.valueOf(messageId)).build(),
                "senderEmail", AttributeValue.builder().s(senderEmail).build(),
                "messageTime", AttributeValue.builder().s(messageTime).build(),
                "isImage", AttributeValue.builder().bool(isImage).build(),
                "messageText", messageText != null ? AttributeValue.builder().s(messageText).build() : AttributeValue.builder().nul(true).build()
        );
    }

    // DynamoDB 조회 결과를 ChatMessage 객체로 변환
    public static ChatMessage fromDynamoDBItem(Map<String, AttributeValue> item) {
        return ChatMessage.builder()
                .chatRoomId(item.get("chatRoomId").s())
                .messageId(Integer.parseInt(item.get("messageId").n()))
                .senderEmail(item.get("senderEmail").s())
                .messageTime(item.get("messageTime").s())
                .isImage(item.get("isImage").bool())
                .messageText(item.containsKey("messageText") && item.get("messageText").s() != null ? item.get("messageText").s() : null)
                .build();
    }
}
