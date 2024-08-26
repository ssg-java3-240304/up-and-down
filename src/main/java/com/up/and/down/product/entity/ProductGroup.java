package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "tbl_product_group")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Destination destination; // 여행지

    private int nights; // 숙박일

    private LocalDate startDate; // 여행 시작일

    // 관리 정보
    private LocalDate createDate; // 생성일

    @Embedded
    @Column(name = "search_keyword")
    private SearchKeyword searchKeywords; // 검색 키워드 set

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_product_group_products",
            joinColumns = @JoinColumn(name = "product_group_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_information")
    private Map<Long, ProductInformation> productList; // 상품 목록

    private int viewCount; // 조회수

    private int likeCount; // 좋아요


    public void increaseViewCount() {
        this.viewCount++;
    }

    // 가장 저렴한 객체 반환
    private ProductInformation getCheapestProduct() {
        return productList.values().stream()
                .min(Comparator.comparing(ProductInformation::getPrice))
                .orElseThrow(() -> new NoSuchElementException("No products found in the product list"));
    }

    // 가장 저렴한 객체 이미지 반환
    public String getCheapestProductImage() {
        return getCheapestProduct().getThumbnailUrl();
    }

    // 제목 반환
    public String getTitle() {
        if (this.nights == 0) {
            return String.format("%s 당일여행", this.destination.getKorName());
        } else {
            return String.format("%s %d박%d일", this.destination.getKorName(), this.nights, this.nights + 1);
        }
    }

    // 내용 반환
    public String getContent() {
        return String.format("출발 예정일 [%s]", this.startDate);
    }

    // 가장 저렴한 객체 이미지 가격 반환
    public String getCheapestProductPrice() {
        return NumberFormat.getInstance().format(getCheapestProduct().getPrice());
    }

    // 가장 저렴한 객체 사이트 반환
    public String getCheapestProductSourceSite() {
        String url = getCheapestProduct().getDetailUrl();
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    // 상품 목록 반환 - 가격이 싼 순서로
    public Collection<ProductInformation> getProductInformationList() {
        return this.productList.values().stream()
                .sorted(Comparator.comparing(ProductInformation::getPrice))
                .collect(Collectors.toList());
    }

    // 저렴한 순으로 4개의 ProductInformation 반환
    public List<ProductInformation> getTop4CheapestProducts() {
        return productList.values().stream()
                .sorted(Comparator.comparing(ProductInformation::getPrice))
                .limit(4)
                .collect(Collectors.toList());
    }


    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
