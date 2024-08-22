package com.up.and.down.main.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import com.up.and.down.search.service.ProductGroupToEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {
    private final ProductGroupDocRepository repo;
    private final ProductGroupToEntityService productGroupToEntityService;

    public List<ProductGroup> findTop4ByOrderByViewCountDesc() {
        this.repo.findTop4ByOrderByViewCountDesc().forEach(System.out::println);
        return this.repo.findTop4ByOrderByViewCountDesc().stream()
                .map(this.productGroupToEntityService::toEntity)
                .collect(Collectors.toList());
    }
}
