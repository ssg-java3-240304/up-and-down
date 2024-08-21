package com.up.and.down.search.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductGroupDocRepository repo;

    public List<ProductGroup> findBySearchKeywordsAndNights(String searchKeywords, String nights) {
        List<ProductGroupDoc> productGroupDocList;

        if (searchKeywords == null || (searchKeywords.isBlank())) {
            if (nights.equals("all")) {
                productGroupDocList = this.repo.findAll();
            } else {
                productGroupDocList = this.repo.findByNights(Integer.parseInt(nights));
            }
        } else {
            if (nights.equals("all")) {
                productGroupDocList = this.repo.findBySearchKeywords(searchKeywords);
            } else {
                productGroupDocList = this.repo.findBySearchKeywordsAndNights(searchKeywords, Integer.parseInt(nights));
            }
        }

        // Doc 객체 -> entity 객체로 변환
        List<ProductGroup> productGroupList = new ArrayList<>();
        for (ProductGroupDoc productGroupDoc : productGroupDocList) {
            productGroupList.add(productGroupDoc.toEntity());
        }

        return productGroupList;
    }
}
