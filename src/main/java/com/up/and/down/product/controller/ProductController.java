package com.up.and.down.product.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.entity.TravelTheme;
import com.up.and.down.product.response.LikeResponse;
import com.up.and.down.product.response.LikeState;
import com.up.and.down.product.service.ProductService;
import com.up.and.down.user.member.entity.Member;
import com.up.and.down.user.member.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @GetMapping
    public String productGroup() {
        log.info("GET productGroup");
        return "product";
    }

    @GetMapping("/{productGroupId}")
    public String productGroupById(
            @PathVariable Long productGroupId,
            Authentication authentication,
            Model model
    ) {
        log.info("GET product - productGroupId: {}", productGroupId);

        // 상품 id로 조회
        ProductGroup productGroup = this.productService.findById(productGroupId);
        // 사용자가 좋아요를 눌렀는지 확인
        boolean isLiked = false;
        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) ((AuthPrincipal) authentication.getPrincipal()).getUser();
            isLiked = member.getLikedProductGroup().contains(productGroupId);
        }
        // 다른 고객이 함께 본 상품 조회
        List<ProductGroup> relatedProductGroups = this.productService.findRelatedProductsTop4(productGroup.getNights());
        // 카테고리 인기 상품 조회
        List<ProductGroup> popularProductGroups = this.productService.findByThemeTop4(productGroup.getSearchKeywords());


        // 상품그룹 등록
        model.addAttribute("productGroup", productGroup);
        model.addAttribute("isLiked", isLiked);

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

        // 인증된 사용자 정보 가져오기
        Member member = (Member) ((AuthPrincipal) authentication.getPrincipal()).getUser();

        // 사용자가 좋아요한 상품 그룹이 포함되어 있는지 확인
        boolean isLiked = member.getLikedProductGroup().contains(productGroupId);

        if (isLiked) {
            // 좋아요가 이미 되어 있는 경우, 좋아요를 취소
            log.info("User is unliked");
            member.getLikedProductGroup().remove(productGroupId);
            userService.save(member); // 사용자 정보 업데이트
            return LikeResponse.builder()
                    .authentication(true)
                    .likeState(LikeState.UN_LIKE)
                    .build();
        } else {
            // 좋아요가 안되어 있는 경우, 좋아요를 추가
            log.info("User is liked");
            member.getLikedProductGroup().add(productGroupId);
            userService.save(member); // 사용자 정보 업데이트
            return LikeResponse.builder()
                    .authentication(true)
                    .likeState(LikeState.DO_LIKE)
                    .build();
        }
    }
}
