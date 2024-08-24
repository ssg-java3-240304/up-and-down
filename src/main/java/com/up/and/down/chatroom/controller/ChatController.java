package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.dto.ChatroomInfoDto;
import com.up.and.down.chatroom.service.ChatroomService;
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
@RequestMapping("/chatroom/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatroomService chatroomService;
    private final ChatService chatService;

    @GetMapping("/{chatroomId}")
    public String chat(
            @PathVariable Long chatroomId,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ){
        log.info("GET chat - chatroomId: {}", chatroomId);

        Long memberId = principal.getUser().getId();
        String nickname = ((Member) principal.getUser()).getNickname();

        chatroomService.addMemberToChatRoom(chatroomId, memberId);
        log.debug("채팅방id{}에 멤버{}추가", chatroomId, memberId);
        ChatroomInfoDto chatroomInfo = this.chatroomService.getChatRoomInfoById(chatroomId);

        model.addAttribute("chatroomName", chatroomInfo.getName());
        model.addAttribute("memberCount", chatroomInfo.getMemberCount());
        model.addAttribute("chatRoomCategories", chatroomInfo.getCategories());

        model.addAttribute("memberId", memberId);
        model.addAttribute("nickname", nickname);

        return "chatroom/chat";
    }

    @GetMapping("/data/{chatroomId}")
    @ResponseBody
    public List<ChatDto> getChatLog(
            @PathVariable Long chatroomId
    ) {
        log.info("GET chat log data - chatroomId: {}", chatroomId);

        List<ChatDto> chatDtoList = this.chatService.findByChatroomId(chatroomId);
        log.debug("chatDtoList: {}", chatDtoList);

        return chatDtoList;
    }
}