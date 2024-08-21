package com.up.and.down.search.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductGroupDocRepository repo;
    private final ProductGroupToEntityService productGroupToEntityService;

    public List<ProductGroup> search(String searchKeyword, String nights, String startDate) {
        List<ProductGroupDoc> productGroupDocList;

        // 검색 키워드, 숙박일수, 시작 날짜를 기준으로 검색
        if (searchKeyword == null || searchKeyword.isBlank()) {
            if ("all".equals(nights)) {
                if (startDate != null && !startDate.isBlank()) {
                    // 검색어가 없고, 숙박일수가 "all"이며, 시작 날짜가 주어진 경우
                    // 시작 날짜 이후의 모든 상품 그룹을 조회
                    productGroupDocList = this.repo.findByStartDateAfter(LocalDate.parse(startDate));
                } else {
                    // 검색어가 없고, 숙박일수가 "all"이며, 시작 날짜가 없는 경우
                    // 모든 상품 그룹을 조회
                    productGroupDocList = this.repo.findAll();
                }
            } else {
                if (startDate != null && !startDate.isBlank()) {
                    // 검색어가 없고, 특정 숙박일수가 있으며, 시작 날짜가 주어진 경우
                    // 해당 숙박일수와 시작 날짜 이후의 상품 그룹을 조회
                    productGroupDocList = this.repo.findByNightsAndStartDateAfter(Integer.parseInt(nights), LocalDate.parse(startDate));
                } else {
                    // 검색어가 없고, 특정 숙박일수만 있는 경우
                    // 해당 숙박일수의 상품 그룹을 조회
                    productGroupDocList = this.repo.findByNights(Integer.parseInt(nights));
                }
            }
        } else {
            if ("all".equals(nights)) {
                if (startDate != null && !startDate.isBlank()) {
                    // 검색어가 있고, 숙박일수가 "all"이며, 시작 날짜가 주어진 경우
                    // 검색어와 시작 날짜 이후의 상품 그룹을 조회
                    productGroupDocList = this.repo.findBySearchKeywordsAndStartDateAfter(searchKeyword, LocalDate.parse(startDate));
                } else {
                    // 검색어가 있고, 숙박일수가 "all"인 경우
                    // 검색어만으로 상품 그룹을 조회
                    productGroupDocList = this.repo.findBySearchKeywords(searchKeyword);
                }
            } else {
                if (startDate != null && !startDate.isBlank()) {
                    // 검색어가 있고, 특정 숙박일수 및 시작 날짜가 주어진 경우
                    // 검색어, 숙박일수, 시작 날짜 이후의 상품 그룹을 조회
                    productGroupDocList = this.repo.findBySearchKeywordsAndNightsAndStartDateAfter(
                            searchKeyword, Integer.parseInt(nights), LocalDate.parse(startDate));
                } else {
                    // 검색어가 있고, 특정 숙박일수만 있는 경우
                    // 검색어와 숙박일수로 상품 그룹을 조회
                    productGroupDocList = this.repo.findBySearchKeywordsAndNights(searchKeyword, Integer.parseInt(nights));
                }
            }
        }

        // Doc 객체 -> entity 객체로 변환
        return productGroupDocList.stream()
                .map(this.productGroupToEntityService::toEntity)
                .collect(Collectors.toList());
    }
}
