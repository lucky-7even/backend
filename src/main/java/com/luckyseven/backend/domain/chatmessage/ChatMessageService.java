package com.luckyseven.backend.domain.chatmessage;

import static com.luckyseven.backend.global.error.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyseven.backend.domain.chatmessage.dto.ChatMessageRequest;
import com.luckyseven.backend.domain.chatmessage.entity.ChatMessage;
import com.luckyseven.backend.domain.chatroom.ChatRoomRepository;
import com.luckyseven.backend.domain.member.MemberRepository;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import com.luckyseven.backend.global.error.exception.BusinessException;

@Service
@Transactional
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;

	private final ChatRoomRepository chatRoomRepository;

	private final MemberRepository memberRepository;

	public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository, MemberRepository memberRepository) {
		this.chatMessageRepository = chatMessageRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.memberRepository = memberRepository;
	}

	public void save(ChatMessageRequest chatMessageRequest) {
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setContent(chatMessage.getContent());
		chatMessage.setMember(memberRepository.findById(chatMessageRequest.getMemberId())
			.orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND)));
		chatMessage.setChatRoom(chatRoomRepository.findById(chatMessageRequest.getChatRoomId())
			.orElseThrow(() -> new BadRequestException(NOT_FOUND_CHAT_ROOM)));

		chatMessageRepository.save(chatMessage);
	}

	public List<ChatMessage> getAllChatMessage(String chatRoomId) {
		return chatMessageRepository.findAllByChatRoom(chatRoomRepository.findById(chatRoomId)
									.orElseThrow(() -> new BusinessException(NOT_FOUND_CHAT_ROOM)));
	}
}
