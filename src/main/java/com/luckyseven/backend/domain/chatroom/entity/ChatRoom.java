package com.luckyseven.backend.domain.chatroom.entity;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class ChatRoom {
    private static final int VALID_HOUR = 3;

    @Id @GeneratedValue
    private String id;

    @NotNull
    @Column(length = 50)
    private String name;

    protected ChatRoom() {
    }

    public ChatRoom(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
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
