package com.up.and.down.search.service;

import com.up.and.down.search.repository.ProductGroupDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductGroupDocRepository repo;
}
