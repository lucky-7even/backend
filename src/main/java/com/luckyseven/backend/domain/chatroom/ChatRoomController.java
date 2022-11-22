package com.luckyseven.backend.domain.chatroom;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luckyseven.backend.domain.chatmessage.ChatMessageService;
import com.luckyseven.backend.domain.chatmessage.entity.ChatMessage;
import com.luckyseven.backend.domain.chatroom.dto.ChatRoomResponse;
import com.luckyseven.backend.domain.chatroom.dto.ChatRoomSaveRequest;

@RequestMapping("api/v1/chatRooms")
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    public ChatRoomController(ChatRoomService chatRoomService, ChatMessageService chatMessageService) {
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
    }

    @GetMapping
    public List<ChatRoomResponse> findAll() {
        return chatRoomService.findAll();
    }

    @GetMapping("/{chatRoomId}")
    public List<ChatMessage> getAllChatMessage(@PathVariable String chatRoomId) {
        return chatMessageService.getAllChatMessage(chatRoomId);
    }

    @PostMapping
    public void addChatRoom(@Valid ChatRoomSaveRequest chatRoomSaveRequest) {
        chatRoomService.save(chatRoomSaveRequest.getName());
    }
}
