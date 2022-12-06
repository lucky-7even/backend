package com.luckyseven.backend.domain.chatmessage;

import javax.validation.Valid;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.luckyseven.backend.domain.chatmessage.dto.ChatMessageRequest;
import com.luckyseven.backend.domain.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/message")
    public void sendMessage(@Payload @Valid ChatMessageRequest chatMessageRequest) {
        chatMessageService.save(chatMessageRequest);
        template.convertAndSend("/topic/chat/" + chatMessageRequest.getChatRoomId(), chatMessageRequest);
    }
}