package com.luckyseven.backend.domain.chatmessage.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.luckyseven.backend.domain.chatroom.entity.ChatRoom;
import com.luckyseven.backend.domain.member.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ChatMessage {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "내용은 필수 값입니다.")
    private String content;

    @NotBlank(message = "송신자는 필수 값입니다.")
    @ManyToOne
    private Member member;

    @ManyToOne
    private ChatRoom chatRoom;

    public ChatMessage() {
    }

}