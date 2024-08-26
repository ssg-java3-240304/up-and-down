package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ShowChatroomDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.service.ChatroomService;
import com.up.and.down.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/chat-api-rooms")
@RequiredArgsConstructor
public class ChatRoomRestController {
    private final ChatroomService chatroomService;

    @GetMapping("all")
    public List<ShowChatroomDto> allChatRoomListByCategory(
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ){
        log.debug("RestController 호출");
        log.debug("keyword : {} ", keyword);
        List<ShowChatroomDto> chatRooms;
        chatRooms = chatroomService.findAllByAsync(categories,keyword);

        return chatRooms;
    }

    @GetMapping("our")
    public List<ShowChatroomDto> ourChatRoomListByCategory(
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        User user = authPrincipal.getUser();
        Long userId = user.getId();

        List<ShowChatroomDto> chatRooms;
        //카테고리가 선택된 경우
        chatRooms = chatroomService.findAllOurChatRoom(categories,keyword,userId);
        return chatRooms;
    }

    @GetMapping("mine")
    public List<ShowChatroomDto> mineChatRoomListByCategory(
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        User user = authPrincipal.getUser();
        Long userId = user.getId();

        List<ShowChatroomDto> chatRooms ;
        //카테고리가 선택된 경우
        chatRooms = chatroomService.findAllMineChatRoom(categories,keyword,userId);

        return chatRooms;
    }
}