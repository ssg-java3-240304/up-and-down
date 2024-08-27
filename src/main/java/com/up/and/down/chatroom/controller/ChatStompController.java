package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.service.ChatroomService;
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
    private final ChatroomService chatRoomService;

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

        // 1. 사용자가 입력한 메시지를 처리
        if (chatDto.getMessage() != null && !chatDto.getMessage().trim().isEmpty()) {
            chatDto.setCreatedAt(LocalDateTime.now());
            this.chatService.save(chatDto);

            // 채팅방 구독자들에게 메시지 전송
            String destination = "/sub/chatroom/chat";
            this.messagingTemplate.convertAndSend(destination, chatDto);
        } else {
            // 사용자가 메시지를 입력하지 않고 방에 들어온 경우 (입장 메시지 처리)
            ChatDto chatdto = new ChatDto();
            chatdto.setChatroomId(chatDto.getChatroomId());
            chatdto.setMemberId(chatDto.getMemberId());
            chatdto.setNickname(chatDto.getNickname());
            chatdto.setMessage(chatDto.getNickname() + "님이 들어왔습니다.");
            chatdto.setCreatedAt(LocalDateTime.now());

            this.chatService.save(chatdto); // 입장 메시지를 DB에 저장

            // 구독자들에게 입장 메시지 전송
            String destination = "/sub/chatroom/chat";
            this.messagingTemplate.convertAndSend(destination, chatdto);
        }
    }
}