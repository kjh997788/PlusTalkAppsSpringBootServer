package com.ChK.PlusTalk.service;

import com.ChK.PlusTalk.dto.FriendRequestDto;
import com.ChK.PlusTalk.dto.FriendResponseDto;
import com.ChK.PlusTalk.dto.MemberResponseDto;
import com.ChK.PlusTalk.entity.Friend;
import com.ChK.PlusTalk.entity.Member;
import com.ChK.PlusTalk.repository.FriendRepository;
import com.ChK.PlusTalk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public MemberResponseDto searchMemberByEmail(String searchingEmail) throws Exception {
        Optional<Member> optionalMember =memberRepository.findByEmail(searchingEmail);
        if (optionalMember.isPresent()){
            Member member = optionalMember.get();
            return MemberResponseDto.builder()
                    .email(member.getEmail())
                    .name(member.getName())
//                    .birthday(member.getBirthday())
                    .profileImageUrl(s3Service.generateGetPreSignedUrl(member.getEmail()))
//                    .intro(member.getIntro())
                    .build();
        } else {
            throw new Exception("회원 정보를 찾을 수 없습니다.");
        }
    }

    public List<MemberResponseDto> searchFriendByEmail(String memberEmail) throws Exception {
        // 특정 회원이 추가한 모든 친구를 조회
        List<Friend> friendList = friendRepository.findAllByMemberEmail(memberEmail);

        if (friendList.isEmpty()) {
            throw new Exception("친구 목록이 없습니다.");
        }

        // 친구 목록을 MemberResponseDto 리스트로 변환
        List<MemberResponseDto> friendResponseList = friendList.stream().map(friend -> {
            Optional<Member> optionalMember = memberRepository.findByEmail(friend.getFriendMemberEmail());
            if (optionalMember.isPresent()) {
                Member friendMember = optionalMember.get();
                return MemberResponseDto.builder()
                        .email(friendMember.getEmail())
                        .name(friendMember.getName())
                        .profileImageUrl(s3Service.generateGetPreSignedUrl(friendMember.getEmail()))
                        .build();
            }
            return null;
        }).filter(dto -> dto != null).toList();

        return friendResponseList;
    }

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
        friendResponseDto.setQuerySuccession(true);
        return friendResponseDto;
    }

    public FriendResponseDto deleteFriendByEmail(String memberEmail, String friendEmail) throws Exception {
        // 먼저 친구 관계를 찾습니다
        Optional<Friend> optionalFriend = friendRepository.findByMemberEmailAndFriendMemberEmail(memberEmail, friendEmail);

        if (optionalFriend.isPresent()) {
            // 존재하면 삭제하고 응답을 구성합니다
            friendRepository.deleteByMemberEmailAndFriendMemberEmail(memberEmail, friendEmail);

            FriendResponseDto friendResponseDto = FriendResponseDto.builder()
                    .memberEmail(memberEmail)
                    .friendMemberEmail(friendEmail)
                    .querySuccession(true)
                    .build();

            return friendResponseDto;
        } else {
            throw new Exception("삭제할 수 없는 친구 관계입니다.");
        }
    }

}
