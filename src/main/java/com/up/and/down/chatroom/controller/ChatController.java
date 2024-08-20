package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.dto.ChatRoomInfoDto;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.chatroom.service.ChatService;
import com.up.and.down.user.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String chat(@PathVariable Long chatRoomId,
                       @AuthenticationPrincipal AuthPrincipal principal,
                       Model model){
        ChatRoomInfoDto chatRoomInfo = chatRoomService.getChatRoomInfo(chatRoomId);

        // 사용자 닉네임 가져오기
        Long memberId = principal.getUser().getId();
        String nickname = chatService.getNicknameById(memberId);

        // 최신 메시지 50개 가져오기
        List<ChatDto> lastMessages = chatService.findLastChatByChatRoomId(chatRoomId, 0, 50);
        model.addAttribute("messages", lastMessages);

        model.addAttribute("chatRoomId", chatRoomId); // 채팅방 id
        model.addAttribute("memberId", memberId); // memberId
        model.addAttribute("nickname", nickname); // 닉네임
        model.addAttribute("chatRoomName", chatRoomInfo.getName()); // 실제 채팅방 이름
        model.addAttribute("chatRoomCategories", chatRoomInfo.getCategories()); // 카테고리
        model.addAttribute("memberCount", chatRoomInfo.getMemberCount()); // 실제 멤버 수

        return "chatroom/chat";
    }

    // 마지막 채팅 메시지 가져오기
    @GetMapping("/{chatRoomId}/messages")
    @ResponseBody
    public List<ChatDto> chatList(@PathVariable("chatRoomId") Long chatRoomId,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "50") int size) {
        log.debug("chatRoomId = {}", chatRoomId);
        List<ChatDto> chatMessages = chatService.findChatMessageByChatRoomId(chatRoomId, page, size);

        // ID가 null로 나오는지 로그 확인
        chatMessages.forEach(chatDto -> log.debug("ChatDto ID: {}", chatDto.getId()));

        return chatMessages;
    }
}