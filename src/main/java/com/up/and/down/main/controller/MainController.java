package com.up.and.down.main.controller;

import com.up.and.down.main.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping
    private String index(){
        log.info("GET index");

        // 검색창

        // 실시간 최다 검색 상품

        // 테마별 상품
        return "index";
    }

    @GetMapping("/search")
    private String search(){
        log.info("GET search");
        return "search/searchList";
    }
}
