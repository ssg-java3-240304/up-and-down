package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

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

    // 관리 정보
    private LocalDate createDate; // 생성일
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "tbl_search_keyword",
            joinColumns = @JoinColumn(name = "product_group_id")
    )
    @Column(name = "search_keyword")
    private Set<SearchKeyword> searchKeywords; // 검색 키워드 set
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_product_group_products",
            joinColumns = @JoinColumn(name = "product_group_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_information")
    private Map<Long, ProductInformation> productList; // 상품 목록
    private int viewCount; // 조회수

    public void increaseViewCount() {
        this.viewCount++;
    }

    // 가장 저렴한 객체 반환
    private ProductInformation getCheapestProduct() {
        return productList.values().stream()
                .min(Comparator.comparing(ProductInformation::getPrice))
                .orElseThrow(() -> new NoSuchElementException("No products found in the product list"));
    }

    // 대표 이미지 반환
    public String getRepresentativeImage() {
        return getCheapestProduct().getThumbnailUrl();
    }

    // 대표 제목 반환
    public String getRepresentativeTitle() {
        return String.format("%s %d박%d일", this.destination.getKrName(), this.nights, this.nights + 1);
    }

    // 대표 내용 반환
    public String getRepresentativeContent() {
        ProductInformation cheapestProduct = getCheapestProduct();
        return String.format("%s [%s]", "여행사", cheapestProduct.getStart_date());
    }

    // 대표 가격 반환
    public String getRepresentativePrice() {
        return NumberFormat.getInstance().format(getCheapestProduct().getPrice());
    }
}
