package com.up.and.down.search.repository;

import com.up.and.down.search.entity.ProductGroupDoc;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductGroupDocRepository extends ElasticsearchRepository<ProductGroupDoc, Long> {
    @Override
    @NonNull
    List<ProductGroupDoc> findAll();

    List<ProductGroupDoc> findBySearchKeywords(String searchKeyword); // 키워드로 검색

    List<ProductGroupDoc> findByNights(int nights); // 숙박일로 검색

    List<ProductGroupDoc> findByStartDate(LocalDate startDate); // 여행출발일로 검색

    List<ProductGroupDoc> findByStartDateAfter(LocalDate startDate); // 여행 출발일 이후 검색

    List<ProductGroupDoc> findBySearchKeywordsAndNights(String searchKeyword, int nights); // 키워드, 숙박일

    List<ProductGroupDoc> findBySearchKeywordsAndStartDateAfter(String searchKeyword, LocalDate startDate); // 키워드, 여행 출발일 이후

    List<ProductGroupDoc> findByNightsAndStartDateAfter(int nights, LocalDate startDate); // 숙박일, 여행 출발일 이후

    List<ProductGroupDoc> findBySearchKeywordsAndNightsAndStartDateAfter(String searchKeyword, int nights, LocalDate startDate); // 키워드, 숙박일, 여행 출발일

    List<ProductGroupDoc> findAllByOrderByViewCountDesc(); // 조회수 순 검색

    List<ProductGroupDoc> findTop4ByOrderByViewCountDesc(); // 조회수 순 상위 4개

    // 페이지네이션
    Page<ProductGroupDoc> findBySearchKeywords(String searchKeyword, Pageable pageable);

    Page<ProductGroupDoc> findByNights(int nights, Pageable pageable);

    Page<ProductGroupDoc> findByStartDateAfter(LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndNights(String searchKeyword, int nights, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndNightsAndStartDateAfter(String searchKeyword, int nights, LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findByNightsAndStartDateAfter(int nights, LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndStartDateAfter(String searchKeyword, LocalDate startDate, Pageable pageable);
}
