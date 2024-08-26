package com.up.and.down.search.repository;

import com.up.and.down.product.entity.Destination;
import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.entity.ProductGroupDoc;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ProductGroupDocRepository extends ElasticsearchRepository<ProductGroupDoc, Long> {
    @Override
    @NonNull
    List<ProductGroupDoc> findAll();

    List<ProductGroupDoc> findBySearchKeywords(String searchKeyword); // 키워드로 검색

    List<ProductGroupDoc> findByNights(int nights); // 숙박일로 검색

    List<ProductGroupDoc> findBySearchKeywordsAndNights(String searchKeyword, int nights); // 키워드, 숙박일

    List<ProductGroupDoc> findTop4ByOrderByViewCountDesc(); // 조회수 순 상위 4개

    // 페이지네이션
    // 페이지네이션: 조회수순으로 조회
    Page<ProductGroupDoc> findBySearchKeywordsOrderByViewCountDesc(String searchKeyword, Pageable pageable);

    Page<ProductGroupDoc> findByNightsOrderByViewCountDesc(int nights, Pageable pageable);

    Page<ProductGroupDoc> findByStartDateAfterOrderByViewCountDesc(LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndNightsOrderByViewCountDesc(String searchKeyword, int nights, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndStartDateAfterOrderByViewCountDesc(String searchKeyword, LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findByNightsAndStartDateAfterOrderByViewCountDesc(int nights, LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndNightsAndStartDateAfterOrderByViewCountDesc(String searchKeyword, int nights, LocalDate startDate, Pageable pageable);

    //  페이지네이션: 좋아요순으로 조회
    Page<ProductGroupDoc> findBySearchKeywordsOrderByLikeCountDesc(String searchKeyword, Pageable pageable);

    Page<ProductGroupDoc> findByNightsOrderByLikeCountDesc(int nights, Pageable pageable);

    Page<ProductGroupDoc> findByStartDateAfterOrderByLikeCountDesc(LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndNightsOrderByLikeCountDesc(String searchKeyword, int nights, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndStartDateAfterOrderByLikeCountDesc(String searchKeyword, LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findByNightsAndStartDateAfterOrderByLikeCountDesc(int nights, LocalDate startDate, Pageable pageable);

    Page<ProductGroupDoc> findBySearchKeywordsAndNightsAndStartDateAfterOrderByLikeCountDesc(String searchKeyword, int nights, LocalDate startDate, Pageable pageable);


    List<ProductGroupDoc> findTop4ByNightsOrderByViewCountDesc(int nights);

    List<ProductGroupDoc> findTop4BySearchKeywordsOrderByLikeCountDesc(String theme);
}