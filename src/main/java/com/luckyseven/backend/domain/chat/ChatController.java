package com.luckyseven.backend.domain.chat;

import javax.validation.Valid;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.luckyseven.backend.domain.chatmessage.ChatMessageService;
import com.luckyseven.backend.domain.chatmessage.entity.ChatMessage;
import com.luckyseven.backend.domain.chatroom.ChatRoomService;

@Controller
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessagingTemplate template, ChatRoomService chatRoomService, ChatMessageService chatMessageService) {
        this.template = template;
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/{chatRoomId}/sendMessage")
    public void sendMessage(@Payload @Valid ChatMessage chatMessage, @DestinationVariable String chatRoomId) {
        chatMessageService.save(chatMessage, chatRoomId);
        template.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
    }
}