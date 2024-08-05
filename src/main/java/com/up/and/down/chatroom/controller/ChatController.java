package com.up.and.down.chatroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chatroom")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    @GetMapping("chat")
    public String chat(){
        log.info("GET /chatroom/chat");
        return "/chatroom/chat";
    }
}
