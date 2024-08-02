package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_product")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // 제목
    private LocalDateTime start_date; // 여행 시작일
    private int nights; // 여행일
    private int price; // 가격
    private String thumbnailUrl; // 여행지 이미지
    private String travelAgency; // 여행사
    private String detailUrl; // 상품상세페이지

    private int viewCount;  // 조회수
    @ElementCollection(targetClass = TravelCategory.class)
    @CollectionTable(
            name = "tbl_product_travel_category",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "travel_category")
    @Enumerated(EnumType.STRING)
    private Set<TravelCategory> travelCategorySet; // 여행 카테고리
    private boolean isVisible; // 페이지 노출 가능 여부

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<ProductComment> commentList; // 상품 댓글 목록
}
