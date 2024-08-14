package com.up.and.down.search.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductGroupDocRepository repo;

    public List<ProductGroup> findByDestinationAndNights(String destination, String nights) {
        List<ProductGroupDoc> productGroupDocList;

        productGroupDocList = this.repo.findAll();

        List<ProductGroup> productGroupList = new ArrayList<>();
        for (ProductGroupDoc productGroupDoc : productGroupDocList) {
            productGroupList.add(productGroupDoc.toEntity());
        }

        return productGroupList;
    }
}
