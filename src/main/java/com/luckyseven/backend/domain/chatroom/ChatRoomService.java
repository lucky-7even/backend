package com.luckyseven.backend.domain.chatroom;

import static com.luckyseven.backend.global.error.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyseven.backend.domain.chatroom.dto.ChatRoomResponse;
import com.luckyseven.backend.domain.chatroom.entity.ChatRoom;
import com.luckyseven.backend.global.error.exception.BusinessException;

@Service
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public void save(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(INVALID_CHAT_ROOM_NAME);
        }
        chatRoomRepository.save(new ChatRoom(name));
    }

    public List<ChatRoomResponse> findAll() {
        return chatRoomRepository.findAll().stream()
                                 .map((chatRoom) -> new ChatRoomResponse(chatRoom))
                                 .collect(Collectors.toList());
    }
}
