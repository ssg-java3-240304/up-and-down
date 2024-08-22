package com.up.and.down.main.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import com.up.and.down.search.service.ProductGroupToEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final ProductGroupDocRepository repo;
    private final ProductGroupToEntityService productGroupToEntityService;

    public List<ProductGroup> findTop4ByOrderByViewCountDesc() {
        log.info("findTop4ByOrderByViewCountDesc start");
        this.repo.findTop4ByOrderByViewCountDesc().forEach(productGroupDoc -> {
            log.debug("productGroupDoc productListJson: {}", productGroupDoc.getProductListJson());
        });
        return this.repo.findTop4ByOrderByViewCountDesc().stream()
                .map(this.productGroupToEntityService::toEntity)
                .collect(Collectors.toList());
    }
}
