package com.up.and.down.search.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.up.and.down.product.entity.Destination;
import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.entity.ProductInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Map;

//@Document(indexName = "product_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGroupDoc {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String searchKeywords; // 검색 키워드

    @Field(type = FieldType.Object)
    private Destination destination; // 여행지

    @Field(type = FieldType.Integer)
    private int nights; // 숙박일

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd")
    private LocalDate startDate; // 여행 시작일

    @Field(type = FieldType.Text)  // JSON 문자열을 저장하기 위한 필드
    private String productListJson; // 상품목록

    @Field(type = FieldType.Integer)
    private int viewCount; // 조회수

    @Field(type = FieldType.Integer)
    private int likeCount; // 좋아요
}
