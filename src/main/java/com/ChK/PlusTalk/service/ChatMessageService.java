package com.ChK.PlusTalk.service;

import com.ChK.PlusTalk.dto.ChatMessageRequestDto;
import com.ChK.PlusTalk.dto.ChatMessageResponseDto;
import com.ChK.PlusTalk.entity.ChatMessage;
import com.ChK.PlusTalk.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final S3Service s3Service;

    public List<ChatMessageResponseDto> getAllMessages(String chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findAllMessagesByChatRoom(chatRoomId);

        return messages.stream()
                .map(message -> ChatMessageResponseDto.builder()
                        .chatRoomId(message.getChatRoomId())
                        .messageId(message.getMessageId())
                        .senderEmail(message.getSenderEmail())
                        .messageTime(message.getMessageTime())
                        .messageText(message.getMessageText())
                        .isImage(message.isImage())
                        .imageUrl(message.isImage() ? s3Service.generatePresignedUrlForChatImage(message.getChatRoomId(), message.getMessageId()) : null)
                        .build())
                .collect(Collectors.toList());
    }

    // 메세지 저장
    public ChatMessageResponseDto createTextMessage(ChatMessageRequestDto requestDto) {
        // messageId 중복 검사
        Optional<ChatMessage> existingMessage = chatMessageRepository.findMessageById(requestDto.getChatRoomId(), requestDto.getMessageId());
        if (existingMessage.isPresent()) {
            throw new IllegalArgumentException("messageId 오류");
        }

        // 현재 시간 (ISO 8601 형식)
        String messageTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        // 메시지 객체 생성
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(requestDto.getChatRoomId())
                .messageId(requestDto.getMessageId())
                .senderEmail(requestDto.getSenderEmail())
                .messageTime(messageTime)
                .isImage(Boolean.TRUE.equals(requestDto.getIsImage()))  // null 처리
                .messageText(requestDto.getMessageText())
                .build();

        // DynamoDB 저장
        chatMessageRepository.save(chatMessage);

        return ChatMessageResponseDto.builder()
                .chatRoomId(chatMessage.getChatRoomId())
                .messageId(chatMessage.getMessageId())
                .senderEmail(chatMessage.getSenderEmail())
                .messageTime(chatMessage.getMessageTime())
                .isImage(chatMessage.isImage())
                .messageText(chatMessage.getMessageText())
                .build();
    }

    public boolean deleteMessage(String chatRoomId, int messageId, String memberEmail) {
        // ✅ messageId로 메시지 조회
        Optional<ChatMessage> messageOptional = chatMessageRepository.findMessageById(chatRoomId, messageId);

        // ✅ 존재하지 않으면 오류 반환
        if (messageOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 메시지가 존재하지 않습니다.");
        }

        ChatMessage message = messageOptional.get();

        // ✅ senderEmail과 memberEmail이 일치하는지 확인
        if (!message.getSenderEmail().equals(memberEmail)) {
            return false; // 삭제 권한 없음 (403 오류 발생)
        }

        // ✅ 삭제 수행
        return chatMessageRepository.deleteMessage(chatRoomId, messageId);
    }

}
