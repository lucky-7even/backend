package com.luckyseven.backend.domain.chatmessage.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.luckyseven.backend.domain.chatroom.entity.ChatRoom;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "메시지 타입은 필수 값입니다.")
    private MessageType type;

    @NotBlank(message = "내용은 필수 값입니다.")
    private String content;

    @NotBlank(message = "송신자는 필수 값입니다.")
    private String sender;

    @ManyToOne
    private ChatRoom chatRoom;

    public enum MessageType {
        CHAT, JOIN, LEAVE, ERROR,
    }

    public ChatMessage() {
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
            "type=" + type +
            ", content='" + content + '\'' +
            ", sender='" + sender + '\'' +
            '}';
    }
}