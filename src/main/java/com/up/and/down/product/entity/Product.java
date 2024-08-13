package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "tbl_product")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private ProductInformation productInformation; // 상품 정보

    // 관리 정보
    private LocalDate createDate; // 생성일
    @Column(name = "source_site", length = 500)
    private String sourceSite; // 출처 페이지
    @Column(name = "view_count")
    private int viewCount;  // 조회수
    @ElementCollection(
            fetch = FetchType.EAGER,
            targetClass = TravelCategory.class
    )
    @CollectionTable(
            name = "tbl_travel_category",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<TravelCategory> travelCategories; // 여행 카테고리
    @Column(name = "is_visible")
    private boolean isVisible; // 페이지 노출 가능 여부
}