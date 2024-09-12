package com.ChK.PlusTalk.service;

import com.ChK.PlusTalk.dto.FriendRequestDto;
import com.ChK.PlusTalk.dto.FriendResponseDto;
import com.ChK.PlusTalk.entity.Friend;
import com.ChK.PlusTalk.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    public FriendResponseDto setFriendByEmail(FriendRequestDto friendRequestDto) {
        Friend friend = new Friend();
        friend.setMemberEmail(friendRequestDto.getMemberEmail());
        friend.setFriendMemberEmail(friendRequestDto.getFriendMemberEmail());
        friend.setFriendSetTime(LocalDateTime.now());
        Friend savedFriend = friendRepository.save(friend);

        FriendResponseDto friendResponseDto = new FriendResponseDto();
        friendResponseDto.setMemberEmail(savedFriend.getMemberEmail());
        friendResponseDto.setFriendMemberEmail(savedFriend.getFriendMemberEmail());
        friendResponseDto.setFriendSetTime(savedFriend.getFriendSetTime());

        return friendResponseDto;
    }


}
