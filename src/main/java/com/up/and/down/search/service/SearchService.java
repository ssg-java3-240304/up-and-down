package com.up.and.down.search.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductGroupDocRepository repo;
    private final ProductGroupToEntityService productGroupToEntityService;

    // 페이지네이션을 위한 search 메서드
    public Page<ProductGroup> search(String searchKeyword, String nights, String startDate, Pageable pageable) {
        Page<ProductGroupDoc> productGroupDocPage;

        // 검색 키워드, 숙박일수, 시작 날짜를 기준으로 페이지 단위로 검색
        if (searchKeyword == null || searchKeyword.isBlank()) {
            if (nights == null || "all".equals(nights) || nights.isBlank()) {
                if (startDate != null && !startDate.isBlank()) {
                    // 검색어가 없고, 숙박일수가 "all"이며, 시작 날짜가 주어진 경우
                    // 시작 날짜 이후의 모든 상품 그룹을 조회 (페이지 단위)
                    productGroupDocPage = this.repo.findByStartDateAfter(LocalDate.parse(startDate), pageable);
                } else {
                    // 검색어가 없고, 숙박일수가 "all"이며, 시작 날짜가 없는 경우
                    // 모든 상품 그룹을 페이지 단위로 조회
                    productGroupDocPage = this.repo.findAll(pageable);
                }
            } else {
                if (startDate != null && !startDate.isBlank()) {
                    // 검색어가 없고, 특정 숙박일수가 있으며, 시작 날짜가 주어진 경우
                    // 해당 숙박일수와 시작 날짜 이후의 상품 그룹을 페이지 단위로 조회
                    productGroupDocPage = this.repo.findByNightsAndStartDateAfter(Integer.parseInt(nights), LocalDate.parse(startDate), pageable);
                } else {
                    // 검색어가 없고, 특정 숙박일수만 있는 경우
                    // 해당 숙박일수의 상품 그룹을 페이지 단위로 조회
                    productGroupDocPage = this.repo.findByNights(Integer.parseInt(nights), pageable);
                }
            }
        } else {
            if (nights == null || "all".equals(nights) || nights.isBlank()) {
                if (startDate != null && !startDate.isBlank()) {
                    // 검색어가 있고, 숙박일수가 "all"이며, 시작 날짜가 주어진 경우
                    // 검색어와 시작 날짜 이후의 상품 그룹을 페이지 단위로 조회
                    productGroupDocPage = this.repo.findBySearchKeywordsAndStartDateAfter(searchKeyword, LocalDate.parse(startDate), pageable);
                } else {
                    // 검색어가 있고, 숙박일수가 "all"인 경우
                    // 검색어만으로 상품 그룹을 페이지 단위로 조회
                    productGroupDocPage = this.repo.findBySearchKeywords(searchKeyword, pageable);
                }
            } else {
                if (startDate != null && !startDate.isBlank()) {
                    // 검색어가 있고, 특정 숙박일수 및 시작 날짜가 주어진 경우
                    // 검색어, 숙박일수, 시작 날짜 이후의 상품 그룹을 페이지 단위로 조회
                    productGroupDocPage = this.repo.findBySearchKeywordsAndNightsAndStartDateAfter(
                            searchKeyword, Integer.parseInt(nights), LocalDate.parse(startDate), pageable);
                } else {
                    // 검색어가 있고, 특정 숙박일수만 있는 경우
                    // 검색어와 숙박일수로 상품 그룹을 페이지 단위로 조회
                    productGroupDocPage = this.repo.findBySearchKeywordsAndNights(searchKeyword, Integer.parseInt(nights), pageable);
                }
            }
        }

        // Doc 객체 -> entity 객체로 변환하고 Page로 반환
        return productGroupDocPage.map(this.productGroupToEntityService::toEntity);
    }
}

