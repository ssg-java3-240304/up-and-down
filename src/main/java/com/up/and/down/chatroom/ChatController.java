package com.up.and.down.chatroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chatroom")
public class ChatController {

    @GetMapping("chat")
    public String chat(){
        return "chatroom/chat";
    }
}
