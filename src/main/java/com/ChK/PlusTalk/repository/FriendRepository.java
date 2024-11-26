package com.ChK.PlusTalk.repository;

import com.ChK.PlusTalk.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 특정 회원과 친구 관계를 조회
    Optional<Friend> findByMemberEmailAndFriendMemberEmail(String memberEmail, String friendMemberEmail);

    // 특정 회원이 추가한 모든 친구를 조회
    List<Friend> findAllByMemberEmail(String memberEmail);

    // 특정 회원과 친구 관계를 삭제
    void deleteByMemberEmailAndFriendMemberEmail(String memberEmail, String friendMemberEmail);
}
