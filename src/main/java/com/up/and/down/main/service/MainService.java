package com.up.and.down.main.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final ProductGroupDocRepository repo;

    public List<ProductGroup> findTop4ByOrderByViewCountDesc() {
        List<ProductGroupDoc> mostSearchedTop4Doc = this.repo.findTop4ByOrderByViewCountDesc();

        List<ProductGroup> mostSearchedTop4 = new ArrayList<>();
        for (ProductGroupDoc productGroupDoc : mostSearchedTop4Doc) {
            mostSearchedTop4.add(productGroupDoc.toEntity());
        }

        return mostSearchedTop4;
    }
}
