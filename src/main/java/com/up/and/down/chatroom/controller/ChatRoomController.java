package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 메인페이지 전체 목록
    @GetMapping("/list")
    public String list(){
        log.info("GET /chatroom/list");
        return "chatroom/list";
    }
}
