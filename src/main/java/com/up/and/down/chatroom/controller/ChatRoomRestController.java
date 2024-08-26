package com.up.and.down.chatroom.controller;

import co.elastic.clients.elasticsearch._types.query_dsl.PrefixQuery;
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
import org.springframework.http.ResponseEntity;
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
    public Page<ShowChatRoomDto> allChatRoomListByCategory(
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ){
        log.debug("RestController 호출");
        log.debug("keyword : {} ", keyword);
        List<ShowChatroomDto> chatRooms;
        chatRooms = chatroomService.findAllByAsync(categories,keyword);
        Page<ShowChatRoomDto> chatRooms;
        chatRooms = chatRoomService.findAllByAsync(pageable,categories,keyword);

        return chatRooms;
    }

    @GetMapping("our")
    public List<ShowChatroomDto> ourChatRoomListByCategory(
    public Page<ShowChatRoomDto> ourChatRoomListByCategory(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
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
        Page<ShowChatRoomDto> chatRooms;
        chatRooms = chatRoomService.findAllOurChatRoom(pageable,categories,keyword,userId);
        return chatRooms;
    }

    @GetMapping("mine")
    public List<ShowChatroomDto> mineChatRoomListByCategory(
    public Page<ShowChatRoomDto> mineChatRoomListByCategory(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) Set<Category> categories,
            @RequestParam(required = false) String keyword
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        User user = authPrincipal.getUser();
        Long userId = user.getId();

        List<ShowChatroomDto> chatRooms ;
        Page<ShowChatRoomDto> chatRooms ;
        //카테고리가 선택된 경우
        chatRooms = chatroomService.findAllMineChatRoom(categories,keyword,userId);
        chatRooms = chatRoomService.findAllMineChatRoom(pageable,categories,keyword,userId);

        return chatRooms;
    }
}