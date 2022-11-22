package com.luckyseven.backend.domain.chatmessage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luckyseven.backend.domain.chatmessage.entity.ChatMessage;
import com.luckyseven.backend.domain.chatroom.entity.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
