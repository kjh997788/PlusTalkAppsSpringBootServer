package com.ChK.PlusTalk.controller;

import com.ChK.PlusTalk.dto.ChatRoomRequestDto;
import com.ChK.PlusTalk.dto.ChatRoomResponseDto;
import com.ChK.PlusTalk.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        try {
            ChatRoomResponseDto responseDto = chatRoomService.createChatRoom(
                    chatRoomRequestDto.getMemberEmail(),
                    chatRoomRequestDto.getFriendEmail()
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "채팅방 생성 성공",
                            "data", responseDto
                    ));
        } catch (Exception e) {
            log.error("채팅방 생성 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "채팅방 생성 중 오류 발생",
                            "data", ""
                    ));
        }
    }

    // 채팅방 목록 조회 (특정 멤버가 속한 채팅방)
    @PostMapping("/list")
    public ResponseEntity<?> getChatRooms(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        try {
            List<ChatRoomResponseDto> chatRooms = chatRoomService.getChatRooms(chatRoomRequestDto.getMemberEmail());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "채팅방 목록 조회 성공",
                            "data", chatRooms
                    ));
        } catch (Exception e) {
            log.error("채팅방 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "채팅방 목록 조회 중 오류 발생",
                            "data", ""
                    ));
        }
    }

    // 채팅방 삭제 (나가기)
    @PostMapping("/delete")
    public ResponseEntity<?> deleteChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        try {
            chatRoomService.deleteChatRoom(chatRoomRequestDto.getMemberEmail(), chatRoomRequestDto.getChatRoomId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "채팅방 나가기 성공",
                            "data", Map.of(
                                    "memberEmail", chatRoomRequestDto.getMemberEmail(),
                                    "chatRoomId", chatRoomRequestDto.getChatRoomId()
                            )
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "message", e.getMessage(),
                            "data", ""
                    ));
        } catch (Exception e) {
            log.error("채팅방 삭제 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "채팅방 삭제 중 오류 발생",
                            "data", ""
                    ));
        }
    }
}
