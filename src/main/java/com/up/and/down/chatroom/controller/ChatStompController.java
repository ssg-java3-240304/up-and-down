package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatStompController {
    private final ChatService chatService;

    @MessageMapping("{chatRoomId}")
    @SendTo("/sub/{chatRoomId}")
    public ChatDto chat(@DestinationVariable(value = "chatRoomId") Long chatRoomId, ChatDto chatDto){
        log.debug("chatroomId = {}", chatRoomId);
        log.debug("chatDto = {}", chatDto);
        chatService.saveMessage(chatDto); // 메시지 db에 저장
        return chatDto;
    }

    // 채팅방 틀을 띄우기
    @GetMapping("/chatroom/chat")
    public String chat(){
        log.info("GET /chatroom/chat");
        return "/chatroom/chat";
    }
}
