package com.up.and.down.user.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @GetMapping("/dashboard")
    public void dashboard() {}

    @GetMapping("/accountManagement/adminInfo")
    public void adminInfo() {}

    @GetMapping("/accountManagement/memberInfo")
    public void memberInfo() {}

    @GetMapping("/companyInfo")
    public void companyInfo() {}

    @GetMapping("/communityManagement")
    public void communityManagement() {}

    @GetMapping("/statistics")
    public void statistics() {}
}
