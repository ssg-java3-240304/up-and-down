package com.up.and.down.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @GetMapping("/test")
    public String mainLayout() {
        log.info("mainLayout");
        return "admin/refundList";
    }
//
//    @GetMapping("/indexPage")
//    public String indexPage() {
//        log.info("indexPage");
//        return "admin/app/dashboard/index-page";
//    }
//
//    @GetMapping("/list")
//    public String list() {
//        log.info("list");
//        return "admin/app/items/list/items-list-page";
//    }
}
