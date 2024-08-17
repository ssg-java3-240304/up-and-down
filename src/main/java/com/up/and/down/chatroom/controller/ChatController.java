package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.dto.ChatRoomInfoDto;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    /**
     * 웹 브라우저에서 주소를 입력하면 특정 사람에게 알림 메시지를 보내는 역할 (채팅방 페이지 보여주고, 메시지 불러오기)
     *
     * 1. 채팅방 페이지 보여주기
     * 2. 그렇다면 메시지를 불러오는 건 비동기처리로 해야함. -> json
     */

    // 채팅창 페이지 보여주기
    @GetMapping("/chat/{chatRoomId}")
    public String chat(@PathVariable Long chatRoomId, Model model){
        ChatRoomInfoDto chatRoomInfo = chatRoomService.getChatRoomInfo(chatRoomId);

        model.addAttribute("chatRoomId", chatRoomId); // 채팅방 id
        model.addAttribute("chatRoomName", chatRoomInfo.getName()); // 실제 채팅방 이름
        model.addAttribute("chatRoomCategories", chatRoomInfo.getCategories()); // 카테고리
        model.addAttribute("memberCount", chatRoomInfo.getMemberCount()); // 실제 멤버 수

        return "chatroom/chat";
    }

    // 메시지 가져오기
    @GetMapping("/{chatRoomId}/messages")
    @ResponseBody
    public List<ChatDto> chatList(@PathVariable("chatRoomId") Long chatRoomId) {
        log.debug("{chatRoomId} = {}", chatRoomId);
        List<ChatDto> chatMessages = chatService.findChatMessageByChatRoomId(chatRoomId);
        return chatMessages;
    }
}