package com.up.and.down.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AuthController {
    @GetMapping("/auth/login")
    public String login() {
        return "login/memberLogin";
    }

    @GetMapping("/app/auth/login")
    public String checkLgoin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "No Permssion";
        } else return "Permission";
    }
}
