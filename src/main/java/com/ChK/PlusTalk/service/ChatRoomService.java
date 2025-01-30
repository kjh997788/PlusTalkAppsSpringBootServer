package com.ChK.PlusTalk.service;

import com.ChK.PlusTalk.dto.ChatRoomResponseDto;
import com.ChK.PlusTalk.entity.ChatRoom;
import com.ChK.PlusTalk.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomResponseDto createChatRoom(String memberEmail, String friendEmail) {
        // 기존 채팅방 확인
        if (chatRoomRepository.findByMembers(memberEmail, friendEmail).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 채팅방입니다.");
        }

        // ISO 8601 포맷 (yyyy-MM-dd'T'HH:mm:ss)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        // 채팅방 ID 생성
        String chatRoomId = memberEmail.split("@")[0] + "_" + timestamp;

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .memberEmail(memberEmail)
                .friendEmail(friendEmail)
                .createdTime(timestamp)
                .build();

        chatRoomRepository.save(chatRoom);

        return ChatRoomResponseDto.builder()
                .memberEmail(memberEmail)
                .friendEmail(friendEmail)
                .chatRoomId(chatRoomId)
                .createdTime(timestamp)
                .build();
    }

    // 특정 멤버가 속한 모든 채팅방 조회
    public List<ChatRoomResponseDto> getChatRooms(String memberEmail) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByMember(memberEmail);

        return chatRooms.stream()
                .map(chatRoom -> ChatRoomResponseDto.builder()
                        .chatRoomId(chatRoom.getChatRoomId())
                        .memberEmail(chatRoom.getMemberEmail())
                        .friendEmail(chatRoom.getFriendEmail())
                        .createdTime(chatRoom.getCreatedTime())
                        .build())
                .collect(Collectors.toList());
    }

    // chatRoomId로 삭제
    public void deleteChatRoom(String memberEmail, String chatRoomId) {
        boolean deleted = chatRoomRepository.deleteChatRoom(chatRoomId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 채팅방입니다.");
        }

        log.info("채팅방 삭제 완료: chatRoomId={}", chatRoomId);
    }
}
