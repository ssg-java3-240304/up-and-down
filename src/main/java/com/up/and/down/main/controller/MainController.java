package com.up.and.down.main.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/main")
public class MainController {

    @GetMapping
    public String index(){
        log.info("GET index");
        // 실시간 최다 검색 상품
        // 테마별 상품
        return "index";
    }
}
