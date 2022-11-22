package com.luckyseven.backend.domain.chatmessage;

import static com.luckyseven.backend.global.error.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyseven.backend.domain.chatmessage.entity.ChatMessage;
import com.luckyseven.backend.domain.chatroom.ChatRoomRepository;
import com.luckyseven.backend.global.error.exception.BusinessException;

@Service
@Transactional
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;

	private final ChatRoomRepository chatRoomRepository;

	public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
		this.chatMessageRepository = chatMessageRepository;
		this.chatRoomRepository = chatRoomRepository;
	}

	public void save(ChatMessage chatMessage, String chatRoomId) {
		chatMessage.setChatRoom(chatRoomRepository.findById(chatRoomId)
												  .orElseThrow(() -> new BusinessException(NOT_FOUND_CHAT_ROOM)));
		chatMessageRepository.save(chatMessage);
	}

	public List<ChatMessage> getAllChatMessage(String chatRoomId) {
		return chatMessageRepository.findAllByChatRoom(chatRoomRepository.findById(chatRoomId)
									.orElseThrow(() -> new BusinessException(NOT_FOUND_CHAT_ROOM)));
	}
}
