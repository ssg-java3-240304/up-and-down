package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatStompController {
    private final SimpMessagingTemplate messagingTemplate;

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    /**
     * 실제로 메시지가 처리하는 곳(새로운 메시지를 실시간으로 주고받는 컨트롤러)
     * 누군가 메시지를 보내면 이 메시지를 받아서 다른 사람에게 보내는 역할
     */
    @MessageMapping("chatroom/chat") // 메시지가 어디로 가야하는지 정하는 것 ex) chatroomId로 보내며
    public void chatStomp(
            Authentication authentication,
            ChatDto chatDto
    ){
        log.debug("authentication = {}", authentication);
        log.debug("chatDto = {}", chatDto);

        chatDto.setCreatedAt(LocalDateTime.now());

        this.chatService.save(chatDto);

        // 구독자에게 메세지 전송
        this.messagingTemplate.convertAndSend("/sub/chatroom/chat", chatDto);
    }
}
