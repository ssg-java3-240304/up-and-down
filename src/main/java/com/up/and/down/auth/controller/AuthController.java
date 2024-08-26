package com.up.and.down.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AuthController {
    @GetMapping("/auth/login")
    public String loginWithUrl(
            @RequestParam(required = false) String url,
            HttpServletRequest request
    ) {
        log.info("GET login with url {}", url);

        if (url != null) {
            request.getSession().setAttribute("redirectUrl", url);
        }

        return "login/memberLogin";
    }
}
