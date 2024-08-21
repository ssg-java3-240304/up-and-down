package com.up.and.down.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.entity.ProductGroupDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductGroupToEntityService {
    private final ProductListJsonConvertService jsonConvertService;

    public ProductGroup toEntity(ProductGroupDoc productGroupDoc) {
        try {
            return ProductGroup.builder()
                    .id(productGroupDoc.getId())
                    .destination(productGroupDoc.getDestination())
                    .nights(productGroupDoc.getNights())
                    .startDate(productGroupDoc.getStartDate())
                    .productList(jsonConvertService.convertJsonToProductList(productGroupDoc.getProductListJson()))
                    .viewCount(productGroupDoc.getViewCount())
                    .likeCount(productGroupDoc.getLikeCount())
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
