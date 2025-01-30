package com.ChK.PlusTalk.entity;

import lombok.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    private String chatRoomId;
    private String memberEmail;
    private String friendEmail;
    private String createdTime;

    // DynamoDB 저장을 위한 변환
    public Map<String, AttributeValue> toDynamoDBItem() {
        return Map.of(
                "chatRoomId", AttributeValue.builder().s(chatRoomId).build(),
                "memberEmail", AttributeValue.builder().s(memberEmail).build(),
                "friendEmail", AttributeValue.builder().s(friendEmail).build(),
                "createdTime", AttributeValue.builder().s(createdTime).build()
        );
    }

    // DynamoDB 조회 결과를 ChatRoom 객체로 변환
    public static ChatRoom fromDynamoDBItem(Map<String, AttributeValue> item) {
        return ChatRoom.builder()
                .chatRoomId(item.get("chatRoomId").s())
                .memberEmail(item.get("memberEmail").s())
                .friendEmail(item.get("friendEmail").s())
                .createdTime(item.get("createdTime").s())
                .build();
    }
}
