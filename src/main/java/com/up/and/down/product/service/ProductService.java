package com.up.and.down.product.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.entity.SearchKeyword;
import com.up.and.down.product.repository.ProductGroupRepository;
import com.up.and.down.product.repository.ProductRepository;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import com.up.and.down.search.service.ProductGroupToEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductGroupRepository productGroupRepo;

    private final ProductGroupDocRepository productGroupDocRepo;
    private final ProductGroupToEntityService productGroupToEntityService;


    public ProductGroup findById(Long productGroupId) {
        return productGroupRepo.findById(productGroupId).orElse(null);
    }

    public List<ProductGroup> findRelatedProductsTop4(int nights) {
         return this.productGroupDocRepo.findTop4ByNightsOrderByViewCountDesc(nights).stream().map(this.productGroupToEntityService::toEntity).toList();
    }

    public List<ProductGroup> findByThemeTop4(SearchKeyword searchKeywords) {
        String theme = searchKeywords.getKeywordSet().stream()
                .findAny()
                .orElse("");
        return this.productGroupDocRepo.findTop4BySearchKeywordsOrderByLikeCountDesc(theme).stream().map(this.productGroupToEntityService::toEntity).toList();
    }
}
