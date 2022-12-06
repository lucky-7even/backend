package com.luckyseven.backend.domain.chatroom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyseven.backend.domain.chatroom.entity.ChatRoom;

@Service
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public void save(Long productId, Long userId1, Long userId2) {  // userId1 -> 대여하는 사람, userId2 -> 대여받는 사람
        chatRoomRepository.save(new ChatRoom(productId, userId1, userId2));
    }

}
