package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    // 메시지 불러오기
    @GetMapping("/chat/{chatRoomId}")
    public List<ChatDto> findMessages(@PathVariable Long chatRoomId, Model model){
        log.info("GET /chatroom/chat/{}", chatRoomId);
        String chatRoomName = chatService.getChatRoomName(chatRoomId);
        int countMembers = chatService.countMembersInChatRoom(chatRoomId);

        model.addAttribute("chatRoomName", chatRoomName); // 실제 채팅방 이름
        model.addAttribute("memberCount", countMembers); // 실제 멤버 수
        model.addAttribute("roomId", chatRoomId);
        List<ChatDto> chatDtos = chatService.findChatMessageByChatRoomId(chatRoomId);
        log.debug(chatDtos.toString());
        return chatDtos;
    }

}
