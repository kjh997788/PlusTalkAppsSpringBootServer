package com.ChK.PlusTalk.controller;

import com.ChK.PlusTalk.dto.FriendRequestDto;
import com.ChK.PlusTalk.dto.FriendResponseDto;
import com.ChK.PlusTalk.dto.MemberResponseDto;
import com.ChK.PlusTalk.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/search")
    public ResponseEntity<?> searchMemberByEmail(@RequestBody FriendRequestDto friendRequestDto) {
        try {
            String targetEmail = friendRequestDto.getMemberEmail();
            MemberResponseDto memberResponseDto = friendService.searchMemberByEmail(targetEmail);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "회원 검색 성공",
                            "data", memberResponseDto
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "message", "일치하는 회원이 없음",
                            "data", ""
                    ));
        }
    }

    @PostMapping("/list")
    public ResponseEntity<?> searchFriendByEmail(@RequestBody FriendRequestDto friendRequestDto) {
        try {
            String memberEmail = friendRequestDto.getMemberEmail();
            List<MemberResponseDto> friendList = friendService.searchFriendByEmail(memberEmail);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "친구 목록 조회 성공",
                            "data", friendList
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "message", "일치하는 회원이 없음",
                            "data", ""
                    ));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> makeFriendByEmail(@RequestBody FriendRequestDto friendRequestDto) {
        try {
            FriendResponseDto friendResponseDto = friendService.setFriendByEmail(friendRequestDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "친구 추가 성공",
                            "data", friendResponseDto
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", 409,
                            "message", e.getMessage(),
                            "data", ""
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "친구 추가 중 오류 발생",
                            "data", ""
                    ));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteFriendByEmail(@RequestBody FriendRequestDto friendRequestDto) {
        try {
            FriendResponseDto friendResponseDto = friendService.deleteFriendByEmail(
                    friendRequestDto.getMemberEmail(),
                    friendRequestDto.getFriendMemberEmail()
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", 200,
                            "message", "친구 삭제 성공",
                            "data", Map.of(
                                    "memberEmail", friendResponseDto.getMemberEmail(),
                                    "friendMemberEmail", friendResponseDto.getFriendMemberEmail()
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
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "message", "친구 삭제 중 오류 발생",
                            "data", ""
                    ));
        }
    }

}
