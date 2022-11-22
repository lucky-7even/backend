package com.luckyseven.backend.domain.chatroom;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luckyseven.backend.domain.chatroom.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findById(String id);
}
