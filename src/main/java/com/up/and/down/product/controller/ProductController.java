package com.up.and.down.product.controller;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.entity.TravelTheme;
import com.up.and.down.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public String productGroup(Model model) {
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

        // 상품그룹
        model.addAttribute("productGroup", productGroup);

        // 사이드바 테마 노출
        model.addAttribute("themes", TravelTheme.values());

        return "product";
    }
}
