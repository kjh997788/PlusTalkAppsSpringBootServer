package com.ChK.PlusTalk.repository;

import com.ChK.PlusTalk.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByEmail(String email);
}
