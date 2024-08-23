package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.dto.ChatRoomInfoDto;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/chatroom/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @GetMapping()
    public String chat(){
        return "chatroom/chat";
    }

    @GetMapping("/data/{chatroomId}")
    @ResponseBody
    public List<ChatDto> getChatLog(
            @PathVariable Long chatroomId
    ) {
        log.info("GET chat log - chatroomId: {}", chatroomId);
        return null;
    }
}