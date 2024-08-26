package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ShowChatroomDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.service.ChatroomService;
import com.up.and.down.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/chat-api-rooms")
@RequiredArgsConstructor
public class ChatroomRestController {
    private final ChatroomService chatroomService;

    @GetMapping("all")
    public Page<ShowChatroomDto> allChatRoomListByCategory(
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ){
        log.debug("RestController 호출");
        log.debug("keyword : {} ", keyword);
        Page<ShowChatroomDto> chatroomList;
        chatroomList = chatroomService.findAllByAsync(pageable,categories,keyword);

        return chatroomList;
    }

    @GetMapping("our")
    public Page<ShowChatroomDto> ourChatRoomListByCategory(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        User user = authPrincipal.getUser();
        Long userId = user.getId();

        Page<ShowChatroomDto> chatroomList;
        chatroomList = chatroomService.findAllOurChatRoom(pageable,categories,keyword,userId);
        return chatroomList;
    }

    @GetMapping("mine")
    public Page<ShowChatroomDto> mineChatRoomListByCategory(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        User user = authPrincipal.getUser();
        Long userId = user.getId();

        Page<ShowChatroomDto> chatroomList ;
        //카테고리가 선택된 경우
        chatroomList = chatroomService.findAllMineChatRoom(pageable,categories,keyword,userId);

        return chatroomList;
    }
}