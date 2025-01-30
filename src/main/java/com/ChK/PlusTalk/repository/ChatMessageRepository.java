package com.ChK.PlusTalk.repository;

import com.ChK.PlusTalk.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    private final DynamoDbClient dynamoDbClient;
    private final String TABLE_NAME = "PlusTalk-chatmessage"; // DynamoDB 테이블 이름

    // 채팅방 내 모든 메시지 조회
    public List<ChatMessage> findAllMessagesByChatRoom(String chatRoomId) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(TABLE_NAME)
                .keyConditionExpression("chatRoomId = :chatRoomId")
                .expressionAttributeValues(Map.of(
                        ":chatRoomId", AttributeValue.builder().s(chatRoomId).build()
                ))
                .build();

        QueryResponse queryResponse = dynamoDbClient.query(queryRequest);

        return queryResponse.items().stream()
                .map(ChatMessage::fromDynamoDBItem)
                .collect(Collectors.toList());
    }

    // ✅ 특정 메시지가 존재하는지 확인
    public Optional<ChatMessage> findMessageById(String chatRoomId, int messageId) {
        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(
                        "chatRoomId", AttributeValue.builder().s(chatRoomId).build(),
                        "messageId", AttributeValue.builder().n(String.valueOf(messageId)).build()
                ))
                .build();

        GetItemResponse getResponse = dynamoDbClient.getItem(getRequest);

        if (getResponse.hasItem() && !getResponse.item().isEmpty()) {
            return Optional.of(ChatMessage.fromDynamoDBItem(getResponse.item()));
        }
        return Optional.empty();
    }

    // 메시지 저장
    public void save(ChatMessage chatMessage) {
        PutItemRequest putRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(chatMessage.toDynamoDBItem())
                .build();

        dynamoDbClient.putItem(putRequest);
    }

    public boolean deleteMessage(String chatRoomId, int messageId) {
        try {
            DeleteItemRequest deleteRequest = DeleteItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(Map.of(
                            "chatRoomId", AttributeValue.builder().s(chatRoomId).build(),
                            "messageId", AttributeValue.builder().n(String.valueOf(messageId)).build()
                    ))
                    .build();

            DeleteItemResponse response = dynamoDbClient.deleteItem(deleteRequest);
            return response.sdkHttpResponse().isSuccessful();
        } catch (DynamoDbException e) {
            return false;
        }
    }
}
