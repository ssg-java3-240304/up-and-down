package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatStompController {
    private final ChatService chatService;

    @MessageMapping("/chat/chatroom/{roomId}")
    @SendTo("/sub/chat/chatroom/{roomId}")
    public ChatDto chat(@DestinationVariable(value = "chatroomId") Long chatroomId, ChatDto chatDto){
        log.debug("chatroomId = {}", chatroomId);
        log.debug("chatDto = {}", chatDto);
        chatService.saveMessage(chatDto); // 메시지 db에 저장
        return chatDto;
    }
}
