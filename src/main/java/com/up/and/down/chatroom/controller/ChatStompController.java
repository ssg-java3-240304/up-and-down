package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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

        // 생성시간 입력
        chatDto.setCreatedAt(LocalDateTime.now());

        // 1. 멤버 id 가져오기
//        Long memberId = ((AuthPrincipal) authentication.getPrincipal()).getUser().getId();

        // 2. 채팅방에 멤버 추가하기

        // db에서 member조회하기

//        chatDto.setCreatedAt(LocalDateTime.now());  // 현재 시간을 메시지에 설정

//        chatService.saveMessage(chatDto); // 메시지 db에 저장

        messagingTemplate.convertAndSend("/sub/chatroom/chat", chatDto);
    }
}
