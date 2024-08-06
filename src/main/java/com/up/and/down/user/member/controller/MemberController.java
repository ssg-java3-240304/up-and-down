package com.up.and.down.user.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/join/member")
@Slf4j
public class MemberController {
    @GetMapping("/terms")
    public String terms() {
        return "join/member/terms";
    }

    @GetMapping("/privacyInfo")
    public String privacyInfo() {
        return "join/member/privacyInfo";
    }

    @GetMapping("/memberAccountInfo")
    public String memberAccountInfo() {
        return "join/member/memberAccountInfo";
    }

    @GetMapping("/registMember")
    public String registMember() {
        return "join/member/registMember";
    }
}
