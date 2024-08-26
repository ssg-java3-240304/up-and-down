package com.up.and.down.search.controller;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @GetMapping
    public void searchProduct(
            @RequestParam(name = "searchKeywords") String searchKeywords,
            @RequestParam(name = "nights") String nights,
            Model model
    ) {
        log.info("GET search - Destination: {}, Nights: {}", searchKeywords, nights);

        // 여행지, 숙박일로 elasticsearch 에 조회
        List<ProductGroup> searchResult = this.service.findBySearchKeywordsAndNights(searchKeywords, nights);
        log.debug("search result: {}", searchResult);
    }

    @GetMapping("/{theme}")
    public String searchByTheme(
            @PathVariable("theme") String theme,
            Model model
    ) {
        log.info("GET search - theme: {}", theme);

        // 테마로 검색 결과 조회

        // 검색 결과를 모델에 추가

        return "search";
    }
}