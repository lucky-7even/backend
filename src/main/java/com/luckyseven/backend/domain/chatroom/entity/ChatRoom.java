package com.luckyseven.backend.domain.chatroom.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.luckyseven.backend.domain.chatmessage.entity.ChatMessage;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ChatRoom {

    @Id @GeneratedValue
    private String id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    protected ChatRoom() {
    }

    public ChatRoom(Long productId, Long userId1, Long userId2) { // userId1 -> 대여하는 사람, userId2 -> 대여받는 사람
        this.id = productId + "-" + userId1 + "-" + userId2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(id, chatRoom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
