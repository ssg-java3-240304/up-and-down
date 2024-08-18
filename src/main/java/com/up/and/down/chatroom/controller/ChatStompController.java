package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatStompController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    /**
     * 실제로 메시지가 처리하는 곳(새로운 메시지를 실시간으로 주고받는 컨트롤러)
     * 누군가 메시지를 보내면 이 메시지를 받아서 다른 사람에게 보내는 역할
     * @param chatRoomId
     * @param chatDto
     * @return
     */
    @MessageMapping("/chat-rooms/chat/{chatRoomId}") // 메시지가 어디로 가야하는지 정하는 것 ex) chatroomId로 보내며
    public void chat(@DestinationVariable(value = "chatRoomId") Long chatRoomId, ChatDto chatDto){
        log.debug("chatroomId = {}", chatRoomId);
        log.debug("chatDto = {}", chatDto);
        chatDto.setNow(LocalDateTime.now());  // 현재 시간을 메시지에 설정
        chatService.saveMessage(chatDto); // 메시지 db에 저장
        messagingTemplate.convertAndSend("/sub/chat-rooms/" + chatRoomId, chatDto);
    }
}
