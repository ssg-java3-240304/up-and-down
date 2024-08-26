package com.up.and.down.search.controller;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.entity.TravelTheme;
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
            @RequestParam(defaultValue = "viewCount") String searchSort,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Model model
    ) {
        log.info("GET search - Destination: {}, Nights: {}, StartDate: {}", searchKeyword, nights, startDate);

        // 여행지, 숙박일로 elasticsearch 에 조회
        Page<ProductGroup> searchResult = switch (SearchSort.fromString(searchSort)) {
            case VIEW_COUNT -> this.service.searchOrderByViewCount(searchKeyword, nights, startDate, pageable);
            case LIKE_COUNT -> this.service.searchOrderByLikeCount(searchKeyword, nights, startDate, pageable);
        };

        // 검색어 타이틀
        model.addAttribute("searchKeywordTitle", (searchKeyword == null || searchKeyword.isBlank()) ? "어디든지" : searchKeyword);
        // 검색 결과 개수
        model.addAttribute("searchResultCount", searchResult.getTotalElements());
        // 검색 결과
        model.addAttribute("searchResult", searchResult);

        // 사이드바 테마 노출
        model.addAttribute("themes", TravelTheme.values());

        // 현재 페이지 정보
        model.addAttribute("currentPage", searchResult.getNumber());
        // 검색어
        model.addAttribute("searchKeyword", searchKeyword);
        // 숙박일
        model.addAttribute("nights", nights);
        // 출발 예정일
        model.addAttribute("startDate", startDate);
        // 정렬 기준
        model.addAttribute("searchSort", searchSort);
        // 총 페이지 수
        model.addAttribute("totalPages", searchResult.getTotalPages());

        return "search";
    }

    @GetMapping("/{theme}")
    public String searchByTheme(
            @PathVariable String theme,
            @RequestParam(defaultValue = "viewCount") String searchSort,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Model model
    ) {
        log.info("GET search - theme: {}", theme);

        theme = theme.toUpperCase();
        TravelTheme travelTheme = TravelTheme.valueOf(theme);

        Page<ProductGroup> searchResult = switch (SearchSort.fromString(searchSort)) {
            case VIEW_COUNT -> this.service.searchByThemeOrderByViewCount(travelTheme.getDisplayKorName(), pageable);
            case LIKE_COUNT -> this.service.searchByThemeOrderByLikeCount(travelTheme.getDisplayKorName(), pageable);
        };

        // 검색어 타이틀
        model.addAttribute("searchKeywordTitle", travelTheme.getDisplayKorName());
        // 검색 결과 개수
        model.addAttribute("searchResultCount", searchResult.getTotalElements());
        // 검색 결과
        model.addAttribute("searchResult", searchResult);

        // 사이드바 테마 노출
        model.addAttribute("themes", TravelTheme.values());

        // 현재 페이지 정보
        model.addAttribute("currentPage", searchResult.getNumber());
        // 검색어
        model.addAttribute("searchKeyword", travelTheme.getDisplayKorName());
        // 숙박일
        model.addAttribute("nights", null);
        // 출발 예정일
        model.addAttribute("startDate", null);
        // 정렬 기준
        model.addAttribute("searchSort", searchSort);
        // 총 페이지 수
        model.addAttribute("totalPages", searchResult.getTotalPages());

        return "search";
    }
}