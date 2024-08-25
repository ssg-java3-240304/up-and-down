package com.up.and.down.product.controller;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.entity.TravelTheme;
import com.up.and.down.product.response.LikeResponse;
import com.up.and.down.product.response.LikeState;
import com.up.and.down.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public String productGroup() {
        log.info("GET productGroup");
        return "product";
    }

    @GetMapping("/{productGroupId}")
    public String productGroupById(
            @PathVariable Long productGroupId,
            Model model
    ) {
        log.info("GET product - productGroupId: {}", productGroupId);

        // 상품 id로 조회
        ProductGroup productGroup = this.service.findById(productGroupId);
        // 다른 고객이 함께 본 상품 조회
        List<ProductGroup> relatedProductGroups = this.service.findRelatedProductsTop4(productGroup.getNights());
        // 카테고리 인기 상품 조회
        List<ProductGroup> popularProductGroups = this.service.findByThemeTop4(productGroup.getSearchKeywords());


        // 상품그룹 등록
        model.addAttribute("productGroup", productGroup);
        // 다른 고객이 함께 본 상품 등록
        model.addAttribute("relatedProductGroups", relatedProductGroups);
        // 카테고리 인기 상품 등록
        model.addAttribute("popularProductGroups", popularProductGroups);

        // 사이드바 테마 노출
        model.addAttribute("themes", TravelTheme.values());

        return "product";
    }

    @GetMapping("/source")
    public String sourceSiteRedirect(
            @RequestParam String redirectUrl
    ) {
        log.info("GET product - redirectUrl: {}", redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/like/{productGroupId}")
    @ResponseBody
    public LikeResponse like(
            @PathVariable Long productGroupId,
            Authentication authentication
    ) {
        log.info("GET product - like productGroupId: {}", productGroupId);

        // 로그인이 되어 있지 않은 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("User is not authenticated");
            return LikeResponse.builder()
                    .authentication(false)
                    .likeState(LikeState.NOT_LIKED)
                    .build();
        }

        return new LikeResponse();
    }
}
