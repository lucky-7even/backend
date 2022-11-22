package com.luckyseven.backend.domain.chatroom.dto;

import com.luckyseven.backend.domain.chatroom.entity.ChatRoom;

public class ChatRoomResponse {
    private final String id;
    private final String name;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
