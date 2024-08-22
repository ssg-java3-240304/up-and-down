package com.up.and.down.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductInformation {
    @Enumerated(EnumType.STRING)
    @Field(type = FieldType.Object)
    private Destination destination; // 여행지

    private Integer nights; // 여행일

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd")
    private LocalDate startDate; // 여행 시작일

    private String title; // 제목

    private int price; // 가격

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl; // 여행지 이미지

    @Column(name = "travel_agency", length = 500)
    private String travelAgency; // 여행사 이미지

    @Column(name = "detail_url", length = 500)
    private String detailUrl; // 상품상세페이지

    // 가격 반환
    public String getPriceString() {
        return NumberFormat.getInstance().format(this.price);
    }

    // 상품 설명 반환
    public String getContent() {
        return String.format("출발 예정일 [%s]", this.startDate);
    }

    // 상품 상세 페이지 반환
    public String getSourceSite() {
        String url = this.detailUrl;
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }
}