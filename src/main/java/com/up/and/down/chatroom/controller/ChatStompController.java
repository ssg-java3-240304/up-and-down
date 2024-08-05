package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;

    // 채팅에 사용자가 접속을 했을 때, 전체 사용자에게 누가 들어왔는지 보여줘야 한다.
    // -> 누구누구 사용자가 접속했습니다.
    @MessageMapping("/chat/room/{roomId}")
    public void chat(@DestinationVariable(value = "roomId") Long roomId, ChatDto chatDto){
        chatDto.setNow(LocalDateTime.now());
        chatService.saveMessage(chatDto); // 메시지 db에 저장
        simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + roomId, chatDto); // 사용자가 입력했을떄, 채팅방에 있는 사람들에게 모두 보여줘야 한다.
    }
}
