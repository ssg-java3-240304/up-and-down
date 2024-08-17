package com.up.and.down.main.controller;

import com.up.and.down.main.service.MainService;
import com.up.and.down.product.entity.ProductGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final MainService service;

    @GetMapping
    public String index(Model model){
        log.info("GET index");

        // 실시간 최다 검색 상품
        List<ProductGroup> mostSearchedTop4 = this.service.findTop4ByOrderByViewCountDesc();
        log.debug("mostSearchedTop4: {}", mostSearchedTop4);
        model.addAttribute("mostSearchedTop4", mostSearchedTop4);

        // 테마별 상품
        return "index";
    }
}
