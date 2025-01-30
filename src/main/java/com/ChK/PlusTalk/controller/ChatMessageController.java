package com.ChK.PlusTalk.controller;

import com.ChK.PlusTalk.service.S3Service;
import com.ChK.PlusTalk.dto.ChatMessageRequestDto;
import com.ChK.PlusTalk.dto.ChatMessageResponseDto;
import com.ChK.PlusTalk.service.ChatMessageService;
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
@RequestMapping("/chatmessage")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final S3Service s3Service;

    // 채팅방의 모든 메시지 조회
    @PostMapping("/list-all")
    public ResponseEntity<?> getAllMessages(@RequestBody ChatMessageRequestDto chatMessageRequestDto) {
        try {
            List<ChatMessageResponseDto> messages = chatMessageService.getAllMessages(chatMessageRequestDto.getChatRoomId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "채팅 메시지 목록 조회 성공",
                            "data", messages
                    ));
        } catch (Exception e) {
            log.error("채팅 메시지 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "채팅 메시지 목록 조회 중 오류 발생",
                            "data", ""
                    ));
        }
    }

    // 텍스트 메시지 생성
    @PostMapping("/create")
    public ResponseEntity<?> createTextMessage(@RequestBody ChatMessageRequestDto chatMessageRequestDto) {
        try {
            ChatMessageResponseDto responseDto = chatMessageService.createTextMessage(chatMessageRequestDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "채팅 메시지 생성 성공",
                            "data", responseDto
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", 409,
                            "message", "messageId 중복 오류",
                            "data", ""
                    ));
        } catch (Exception e) {
            log.error("채팅 메시지 생성 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "채팅 메시지 생성 중 오류 발생",
                            "data", ""
                    ));
        }
    }

    // 이미지 업로드용 Pre-Signed URL 생성
    @PostMapping("/create-img-url")
    public ResponseEntity<?> generateImageUploadUrl(@RequestBody ChatMessageRequestDto requestDto) {
        try {
            String presignedUrl = s3Service.generatePresignedUrlForChatImageUpload(
                    requestDto.getChatRoomId(), requestDto.getMessageId()
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "이미지 업로드용 Pre-Signed URL 생성 성공",
                            "data", Map.of("uploadUrl", presignedUrl)
                    ));
        } catch (Exception e) {
            log.error("이미지 업로드용 Pre-Signed URL 생성 실패: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "이미지 업로드용 Pre-Signed URL 생성 중 오류 발생",
                            "data", ""
                    ));
        }
    }

    // 메시지 삭제
    @PostMapping("/delete")
    public ResponseEntity<?> deleteMessage(@RequestBody ChatMessageRequestDto requestDto) {
        try {
            boolean deleted = chatMessageService.deleteMessage(
                    requestDto.getChatRoomId(),
                    requestDto.getMessageId(),
                    requestDto.getSenderEmail()
            );

            if (deleted) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of(
                                "status", 200,
                                "message", "채팅 메시지 삭제 성공",
                                "data", Map.of(
                                        "chatRoomId", requestDto.getChatRoomId(),
                                        "messageId", requestDto.getMessageId()
                                )
                        ));
            } else {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "status", 403,
                                "message", "삭제 권한이 없습니다.",
                                "data", ""
                        ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "message", e.getMessage(),
                            "data", ""
                    ));
        } catch (Exception e) {
            log.error("채팅 메시지 삭제 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "채팅 메시지 삭제 중 오류 발생",
                            "data", ""
                    ));
        }
    }

}
