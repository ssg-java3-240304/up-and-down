package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/chatroom")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @GetMapping("chat")
    public String chat(){
        log.info("GET /chatroom/chat");
        return "/chatroom/chat";
    }

    // 메시지 불러오기
    @GetMapping("chat/{roomId}")
    public List<ChatDto> findMessages(@PathVariable Long roomId, Model model){
        log.info("GET /chatroom/chat/{}", roomId);
        model.addAttribute("chatRoomName", "chatRoomName"); // 실제 채팅방 이름
        model.addAttribute("memberCount", "countMembers"); // 실제 멤버 수
        model.addAttribute("roomId", roomId);
        List<ChatDto> chatDtos = chatService.findChatMessageByChatRoomId(roomId);
        log.debug(chatDtos.toString());
        return chatDtos;
    }

}
