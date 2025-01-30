package com.ChK.PlusTalk.repository;

import com.ChK.PlusTalk.entity.ChatRoom;
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
public class ChatRoomRepository {
    private final DynamoDbClient dynamoDbClient;
    private final String TABLE_NAME = "PlusTalk-chatroom";  // DynamoDB 테이블 이름

    // 채팅방 저장
    public void save(ChatRoom chatRoom) {
        PutItemRequest putRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(chatRoom.toDynamoDBItem())
                .build();

        dynamoDbClient.putItem(putRequest);
    }

    // 특정 멤버들이 포함된 채팅방 조회
    public Optional<ChatRoom> findByMembers(String memberEmail, String friendEmail) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("memberEmail = :mEmail AND friendEmail = :fEmail")
                .expressionAttributeValues(Map.of(
                        ":mEmail", AttributeValue.builder().s(memberEmail).build(),
                        ":fEmail", AttributeValue.builder().s(friendEmail).build()
                ))
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
        if (scanResponse.count() > 0) {
            return Optional.of(ChatRoom.fromDynamoDBItem(scanResponse.items().get(0)));
        }
        return Optional.empty();
    }

    // 특정 멤버가 포함된 채팅방 조회
    public List<ChatRoom> findChatRoomsByMember(String memberEmail) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("memberEmail = :email OR friendEmail = :email")
                .expressionAttributeValues(Map.of(
                        ":email", AttributeValue.builder().s(memberEmail).build()
                ))
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        return scanResponse.items().stream()
                .map(ChatRoom::fromDynamoDBItem)
                .collect(Collectors.toList());
    }

    // 채팅방 삭제
    public boolean deleteChatRoom(String chatRoomId) {
        try {
            DeleteItemRequest deleteRequest = DeleteItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(Map.of(
                            "chatRoomId", AttributeValue.builder().s(chatRoomId).build()
                    ))
                    .build();

            DeleteItemResponse response = dynamoDbClient.deleteItem(deleteRequest);
            return response.sdkHttpResponse().isSuccessful();
        } catch (DynamoDbException e) {
            return false;
        }
    }
}
