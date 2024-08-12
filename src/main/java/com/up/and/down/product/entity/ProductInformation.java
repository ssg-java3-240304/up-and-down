package com.up.and.down.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductInformation {
    @Enumerated(EnumType.STRING)
    private Destination destination; // 여행지
    private int nights; // 여행일

    private String title; // 제목
    private LocalDate start_date; // 여행 시작일
    private int price; // 가격

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl; // 여행지 이미지
    @Column(name = "travel_agency", length = 500)
    private String travelAgency; // 여행사 이미지
    @Column(name = "detail_url", length = 500)
    private String detailUrl; // 상품상세페이지
}