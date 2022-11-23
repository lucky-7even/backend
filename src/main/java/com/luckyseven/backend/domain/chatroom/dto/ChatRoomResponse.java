package com.luckyseven.backend.domain.chatroom.dto;

import com.luckyseven.backend.domain.chatroom.entity.ChatRoom;

public class ChatRoomResponse {
    private final Long id;
    private final String name;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
