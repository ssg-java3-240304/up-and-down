package com.up.and.down.search.controller;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @GetMapping
    public String searchProductByParam(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String nights,
            @RequestParam(required = false) String startDate,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Model model
    ) {
        log.info("GET search - Destination: {}, Nights: {}, StartDate: {}", searchKeyword, nights, startDate);

        // 여행지, 숙박일로 elasticsearch 에 조회
        Page<ProductGroup> searchResult = this.service.search(searchKeyword, nights, startDate, pageable);

        // 검색어 타이틀
        model.addAttribute("searchKeywordTitle", (searchKeyword == null || searchKeyword.isBlank()) ? "어디든지" : searchKeyword);
        // 검색 결과 개수
        model.addAttribute("searchResultCount", searchResult.getTotalElements());
        // 검색 결과
        model.addAttribute("searchResult", searchResult);

        // 현재 페이지 정보
        model.addAttribute("currentPage", searchResult.getNumber());
        // 검색어
        model.addAttribute("searchKeyword", searchKeyword);
        // 숙박일
        model.addAttribute("nights", nights);
        // 출발 예정일
        model.addAttribute("startDate", startDate);
        // 총 페이지 수
        model.addAttribute("totalPages", searchResult.getTotalPages());

        return "search";
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