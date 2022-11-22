package com.luckyseven.backend.domain.chatroom;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ChatRoomSaveRequest {
    @NotBlank(message = "채팅방 이름은 필수 값입니다.")
    @Length(max = 23, message = "채팅방 이름의 길이는 최대 23자입니다.")
    private final String name;

    @JsonCreator
    public ChatRoomSaveRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
