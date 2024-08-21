package com.up.and.down.search.repository;

import com.up.and.down.search.entity.ProductGroupDoc;
import lombok.NonNull;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductGroupDocRepository extends ElasticsearchRepository<ProductGroupDoc, Long> {
    @Override
    @NonNull
    List<ProductGroupDoc> findAll();

    List<ProductGroupDoc> findBySearchKeywords(String keyword); // 키워드로 검색

    List<ProductGroupDoc> findByNights(int nights); // 숙박일로 검색

    List<ProductGroupDoc> findByStartDate(LocalDate startDate); // 여행출발일로 검색

    List<ProductGroupDoc> findByStartDateAfter(LocalDate startDate); // 여행 출발일 이후 검색

    List<ProductGroupDoc> findBySearchKeywordsAndNights(String searchKeywords, int nights);

    List<ProductGroupDoc> findAllByOrderByViewCountDesc(); // 조회수 순 검색

    List<ProductGroupDoc> findTop4ByOrderByViewCountDesc(); // 조회수 순 상위 4개
}
