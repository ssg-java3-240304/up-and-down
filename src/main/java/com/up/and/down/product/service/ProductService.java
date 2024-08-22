package com.up.and.down.product.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.repository.ProductGroupRepository;
import com.up.and.down.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductGroupRepository productGroupRepo;


    public ProductGroup findById(Long productGroupId) {
        return productGroupRepo.findById(productGroupId).orElse(null);
    }
}
