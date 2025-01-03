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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/search")
    public ResponseEntity<MemberResponseDto> searchMemberByEmail(@RequestBody FriendRequestDto friendRequestDto) throws Exception {
        String targetEmail = friendRequestDto.getMemberEmail();
        MemberResponseDto memberResponseDto  = friendService.searchMemberByEmail(targetEmail);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @PostMapping("/list")
    public ResponseEntity<List<MemberResponseDto>> searchFriendByEmail(@RequestBody FriendRequestDto friendRequestDto) throws Exception {
        String targetEmail = friendRequestDto.getMemberEmail();
        List<MemberResponseDto> friendList = friendService.searchFriendByEmail(targetEmail);
        return ResponseEntity.status(HttpStatus.OK).body(friendList);
    }

    @PostMapping("/add")
    public ResponseEntity<FriendResponseDto> makeFriendByEmail(@RequestBody FriendRequestDto friendRequestDto) {
        FriendResponseDto friendResponseDto = friendService.setFriendByEmail(friendRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(friendResponseDto);
    }

    @PostMapping("/delete")
    public ResponseEntity<FriendResponseDto> deleteFriendByEmail(@RequestBody FriendRequestDto friendRequestDto) throws Exception {
        String memberEmail = friendRequestDto.getMemberEmail();
        String friendEmail = friendRequestDto.getFriendMemberEmail();
        FriendResponseDto friendResponseDto = friendService.deleteFriendByEmail(memberEmail, friendEmail);
        return ResponseEntity.status(HttpStatus.OK).body(friendResponseDto);
    }

}
