package com.up.and.down.search.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

//@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductGroupDocRepository repo;
    private final ProductGroupToEntityService productGroupToEntityService;

    // 조회수 기준으로 정렬된 검색
    public Page<ProductGroup> searchOrderByViewCount(String searchKeyword, String nights, String startDate, Pageable pageable) {
        Page<ProductGroupDoc> productGroupDocPage;

        if (searchKeyword == null || searchKeyword.isBlank()) {
            if (nights == null || "all".equals(nights) || nights.isBlank()) {
                if (startDate != null && !startDate.isBlank()) {
                    productGroupDocPage = this.repo.findByStartDateAfterOrderByViewCountDesc(LocalDate.parse(startDate), pageable);
                } else {
                    productGroupDocPage = this.repo.findAll(pageable);
                }
            } else {
                if (startDate != null && !startDate.isBlank()) {
                    productGroupDocPage = this.repo.findByNightsAndStartDateAfterOrderByViewCountDesc(Integer.parseInt(nights), LocalDate.parse(startDate), pageable);
                } else {
                    productGroupDocPage = this.repo.findByNightsOrderByViewCountDesc(Integer.parseInt(nights), pageable);
                }
            }
        } else {
            if (nights == null || "all".equals(nights) || nights.isBlank()) {
                if (startDate != null && !startDate.isBlank()) {
                    productGroupDocPage = this.repo.findBySearchKeywordsAndStartDateAfterOrderByViewCountDesc(searchKeyword, LocalDate.parse(startDate), pageable);
                } else {
                    productGroupDocPage = this.repo.findBySearchKeywordsOrderByViewCountDesc(searchKeyword, pageable);
                }
            } else {
                if (startDate != null && !startDate.isBlank()) {
                    productGroupDocPage = this.repo.findBySearchKeywordsAndNightsAndStartDateAfterOrderByViewCountDesc(
                            searchKeyword, Integer.parseInt(nights), LocalDate.parse(startDate), pageable);
                } else {
                    productGroupDocPage = this.repo.findBySearchKeywordsAndNightsOrderByViewCountDesc(searchKeyword, Integer.parseInt(nights), pageable);
                }
            }
        }

        return productGroupDocPage.map(this.productGroupToEntityService::toEntity);
    }

    // 좋아요 수 기준으로 정렬된 검색
    public Page<ProductGroup> searchOrderByLikeCount(String searchKeyword, String nights, String startDate, Pageable pageable) {
        Page<ProductGroupDoc> productGroupDocPage;

        if (searchKeyword == null || searchKeyword.isBlank()) {
            if (nights == null || "all".equals(nights) || nights.isBlank()) {
                if (startDate != null && !startDate.isBlank()) {
                    productGroupDocPage = this.repo.findByStartDateAfterOrderByLikeCountDesc(LocalDate.parse(startDate), pageable);
                } else {
                    productGroupDocPage = this.repo.findAll(pageable);
                }
            } else {
                if (startDate != null && !startDate.isBlank()) {
                    productGroupDocPage = this.repo.findByNightsAndStartDateAfterOrderByLikeCountDesc(Integer.parseInt(nights), LocalDate.parse(startDate), pageable);
                } else {
                    productGroupDocPage = this.repo.findByNightsOrderByLikeCountDesc(Integer.parseInt(nights), pageable);
                }
            }
        } else {
            if (nights == null || "all".equals(nights) || nights.isBlank()) {
                if (startDate != null && !startDate.isBlank()) {
                    productGroupDocPage = this.repo.findBySearchKeywordsAndStartDateAfterOrderByLikeCountDesc(searchKeyword, LocalDate.parse(startDate), pageable);
                } else {
                    productGroupDocPage = this.repo.findBySearchKeywordsOrderByLikeCountDesc(searchKeyword, pageable);
                }
            } else {
                if (startDate != null && !startDate.isBlank()) {
                    productGroupDocPage = this.repo.findBySearchKeywordsAndNightsAndStartDateAfterOrderByLikeCountDesc(
                            searchKeyword, Integer.parseInt(nights), LocalDate.parse(startDate), pageable);
                } else {
                    productGroupDocPage = this.repo.findBySearchKeywordsAndNightsOrderByLikeCountDesc(searchKeyword, Integer.parseInt(nights), pageable);
                }
            }
        }

        return productGroupDocPage.map(this.productGroupToEntityService::toEntity);
    }

    public Page<ProductGroup> searchByThemeOrderByViewCount(String theme, Pageable pageable) {
        return this.repo.findBySearchKeywordsOrderByViewCountDesc(theme, pageable).map(this.productGroupToEntityService::toEntity);
    }

    public Page<ProductGroup> searchByThemeOrderByLikeCount(String theme, Pageable pageable) {
        return this.repo.findBySearchKeywordsOrderByLikeCountDesc(theme, pageable).map(this.productGroupToEntityService::toEntity);
    }
}

