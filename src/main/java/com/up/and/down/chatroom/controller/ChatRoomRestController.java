package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ShowChatRoomDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/chat-api-rooms")
@RequiredArgsConstructor
public class ChatRoomRestController {
    private final ChatRoomService chatRoomService;

    @GetMapping("all")
    public List<ShowChatRoomDto> allChatRoomListByCategory(
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ){
        log.debug("RestController 호출");
        log.debug("keyword : {} ", keyword);
        List<ShowChatRoomDto> chatRooms;
        chatRooms = chatRoomService.findAllByAsync(categories,keyword);

        return chatRooms;
    }

    @GetMapping("our")
    public List<ShowChatRoomDto> ourChatRoomListByCategory(
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        User user = authPrincipal.getUser();
        Long userId = user.getId();

        List<ShowChatRoomDto> chatRooms;
        //카테고리가 선택된 경우
        chatRooms = chatRoomService.findAllOurChatRoom(categories,keyword,userId);
        return chatRooms;
    }

    @GetMapping("mine")
    public List<ShowChatRoomDto> mineChatRoomListByCategory(
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        User user = authPrincipal.getUser();
        Long userId = user.getId();

        List<ShowChatRoomDto> chatRooms ;
        //카테고리가 선택된 경우
        chatRooms = chatRoomService.findAllMineChatRoom(categories,keyword,userId);

        return chatRooms;
    }
}