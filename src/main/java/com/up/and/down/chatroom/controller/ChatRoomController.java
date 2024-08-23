package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatRoomListResponseDto;
import com.up.and.down.chatroom.dto.ChatRoomRegistRequestDto;
import com.up.and.down.chatroom.dto.ChatRoomResponseDto;
import com.up.and.down.chatroom.dto.ShowChatRoomDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.common.paging.PageCriteria;
import com.up.and.down.user.User;
import com.up.and.down.user.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;


@Slf4j
@Controller
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/loginCheck")
    @ResponseBody
    public String checkLgoin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "NoPermission";
        } else return "Permission";
    }

    //메인 페이지 조회 -> 동기처리
    @GetMapping("/list")
    public String findAllChatRoomList(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Model model
    ){

        Page<ShowChatRoomDto> chatRooms = chatRoomService.findAllChatRooms(pageable);
        model.addAttribute("chatRooms", chatRooms);

        return "chatroom/list";
    }



}


